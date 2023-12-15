package ru.yandex.practicum.filmorate.dao.dbimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MPARatingDao;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mpaDbStorage")
@Repository
public class MPARatingDaoDbImpl extends AbstractDaoDbImpl<MPARating> implements MPARatingDao {

    public MPARatingDaoDbImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        flushCache();
    }

    public void flushCache() {
        String sqlQuery = "SELECT ID, NAME FROM RATINGS_MPA ORDER BY 1;";
        List<MPARating> mpaRatings =  jdbcTemplate.query(sqlQuery, this::rowMapper);
        data.clear();
        for (MPARating mpaRating : mpaRatings) {
            data.put(mpaRating.getId(), mpaRating);
        }
    }

    private MPARating rowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return MPARating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}