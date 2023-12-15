package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.service.Message.FILM_NOT_FOUND_MESSAGE;

@Component("filmDbStorage")
@Repository
@Slf4j
public class FilmDaoDbImpl extends BasicDaoDbImpl<Film> implements FilmDao {

    @Autowired
    public FilmDaoDbImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Map<Integer, Film> get() {
        log.debug("Получаем все фильмы из БД..");
        String sqlQuery = "SELECT * FROM films ORDER BY 1;";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::filmRowMapper);
        Map<Integer, Film> filmsMap = new HashMap<>();
        for (Film film : films) {
            filmsMap.put(film.getId(), film);
        }
        log.debug("Получили фильмы из БД: {}", filmsMap);
        return filmsMap;
    }

    @Override
    public Film get(int id) {
        log.debug("Получаем фильм из БД по id: {}", id);
        String sqlQuery = "SELECT * FROM Films WHERE id = ?;";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::filmRowMapper, id);
        log.debug("Получили фильм из БД по id {} : {}", id, films);
        if (films.size() == 0) {
            log.warn(FILM_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(FILM_NOT_FOUND_MESSAGE);
        }
        return films.get(0);
    }

    @Override
    public void put(int id, Film value) {
        Film film = (Film) value;
        log.debug("Создаем/изменяем фильм в БД по id {} : {}", id, film);
        int filmId = mergeFilm(film);
        film.setId(filmId);
        deleteFilmGenres(film);
        insertFilmGenres(film);
        deleteFilmLikes(film);
        insertFilmLikes(film);
        log.debug("Создано/изменено в БД по id {} : {}", id, film);
    }

    private int mergeFilm(Film film) {
        String sqlQuery = "MERGE INTO films (id, name, description, release_date, duration, rating_id) KEY(id) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setInt(1, film.getId());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private void deleteFilmGenres(Film film) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?;";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, film.getId());
            return stmt;
        });
    }

    private void insertFilmGenres(Film film) {
        List<Integer> genreIds = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            genreIds.add(genre.getId());
        }
        String sqlQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?,?);";
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, film.getId());
                preparedStatement.setInt(2, genreIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return genreIds.size();
            }
        });
    }

    private void deleteFilmLikes(Film film) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ?;";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, film.getId());
            return stmt;
        });
    }

    private void insertFilmLikes(Film film) {
        List<Integer> userIds = new ArrayList<>(film.getLikes());
        String sqlQuery = "INSERT INTO film_likes (film_id, user_id) VALUES (?,?);";
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, film.getId());
                preparedStatement.setInt(2, userIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return userIds.size();
            }
        });
    }

    private Film filmRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new MPARating(resultSet.getInt("rating_id"), ""))
                .genres(getGenres(resultSet.getInt("id")))
                .likes(getLikes(resultSet.getInt("id")))
                .build();
    }

    private Set<Genre> getGenres(int id) {
        String sqlQuery = "SELECT genre_id FROM film_genres WHERE film_id = ? ORDER BY 1;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::genresRowMapper, id));
    }

    private Genre genresRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), "");
    }

    private Set<Integer> getLikes(int id) {
        String sqlQuery = "SELECT user_id FROM film_likes WHERE film_id = ? ORDER BY 1;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::likesRowMapper, id));
    }

    private Integer likesRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }
}
