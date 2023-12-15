package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MPARatingDaoDbImplTest {
    private final JdbcTemplate jdbcTemplate;
    private MPARatingDaoDbImpl mpaDao;

    @BeforeEach
    void beforeEach() {
        mpaDao = new MPARatingDaoDbImpl(jdbcTemplate);
    }

    @Test
    public void getGenreById() {
        MPARating mpaRating = mpaDao.get(2);
        assertThat(mpaRating).hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    public void getAllGenres() {
        Map<Integer, MPARating> mpaRating = mpaDao.get();
        assertEquals(mpaRating.size(), 5);
    }
}