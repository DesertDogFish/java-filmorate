package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreDaoDbImplTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDaoDbImpl genreDao;

    @BeforeEach
    void beforeEach() {
        genreDao = new GenreDaoDbImpl(jdbcTemplate);
    }

    @Test
    public void getGenreById() {
        Genre genre = genreDao.get(1);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void getAllGenres() {
        Map<Integer, Genre> genres = genreDao.get();
        assertEquals(genres.size(), 6);
    }
}