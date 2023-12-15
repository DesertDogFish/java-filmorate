package ru.yandex.practicum.filmorate.dao.dbimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genreDbStorage")
@Repository
public class GenreDaoDbImpl extends AbstractDaoDbImpl<Genre> implements GenreDao {

    public GenreDaoDbImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        flushCache();
    }

    public void flushCache() {
        String sqlQuery = "SELECT ID, NAME FROM GENRES ORDER BY 1;";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::rowMapper);
        data.clear();
        for (Genre genre : genres) {
            data.put(genre.getId(), genre);
        }
    }

    private Genre rowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }
}