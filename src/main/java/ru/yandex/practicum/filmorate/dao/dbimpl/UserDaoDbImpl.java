package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Component("userDbStorage")
@Repository
@Slf4j
public class UserDaoDbImpl extends BasicDaoDbImpl<User> implements UserDao {

    @Autowired
    public UserDaoDbImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Map<Integer, AbstractIdModel> get() {
        log.debug("Получаем всех юзеров из БД..");
        String sqlQuery = "SELECT * FROM users ORDER BY 1;";
        List<User> users = jdbcTemplate.query(sqlQuery, this::userRowMapper);
        Map<Integer, AbstractIdModel> usersMap = new HashMap<>();
        for (User user : users) {
            usersMap.put(user.getId(), user);
        }
        log.debug("Получили юзеров из БД: {}", usersMap);
        return usersMap;
    }

    @Override
    public AbstractIdModel get(int id) {
        log.debug("Получаем юзера из БД по id: {}", id);
        String sqlQuery = "SELECT * FROM users WHERE id = ?;";
        List<User> users = jdbcTemplate.query(sqlQuery, this::userRowMapper, id);
        log.debug("Получили юзера из БД по id {} : {}", id, users);
        if (users.size() == 0) {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return users.get(0);
    }

    @Override
    public void put(int id, AbstractIdModel value) {
        User user = (User) value;
        log.debug("Создаем/изменяем юзера в БД по id {} : {}", id, user);
        int userId = mergeUser(user);
        user.setId(userId);
        deleteFriends(user);
        insertFriends(user);
        log.debug("Создано/изменено в БД по id {} : {}", id, user);
    }

    private int mergeUser(User user) {
        String sqlQuery = "MERGE INTO users (id, email, login, name, birthday) KEY(id) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getName());
            stmt.setDate(5, java.sql.Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private void deleteFriends(User user) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ?;";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, user.getId());
            return stmt;
        });
    }

    private void insertFriends(User user) {
        List<Integer> friendIds = new ArrayList<>(user.getFriends());
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?,?);";
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setInt(2, friendIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return friendIds.size();
            }
        });
    }

    private User userRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(getFriends(resultSet.getInt("id")))
                .build();
    }

    private Set<Integer> getFriends(int id) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ? ORDER BY 1;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::friendsRowMapper, id));
    }

    private Integer friendsRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }
}
