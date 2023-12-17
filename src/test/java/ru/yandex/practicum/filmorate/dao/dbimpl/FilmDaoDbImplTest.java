package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDaoDbImplTest {
    private final JdbcTemplate jdbcTemplate;

    private final Film film1 = Film.builder()
            .name("Test Film")
            .description("Test Description")
            .releaseDate(LocalDate.of(2021, 1, 1))
            .duration(120)
            .mpa(MPARating.builder()
                    .id(1)
                    .name("")
                    .build())
            .genres(Set.of(Genre.builder()
                    .id(1)
                    .name("")
                    .build()))
            .likes(Set.of(1))
            .build();
    private final Film film2 = Film.builder()
            .name("Test Film2")
            .description("Test Description2")
            .releaseDate(LocalDate.of(2022, 12, 12))
            .duration(180)
            .mpa(MPARating.builder()
                    .id(2)
                    .name("")
                    .build())
            .genres(Set.of(Genre.builder()
                            .id(5)
                            .name("")
                            .build(),
                    Genre.builder()
                            .id(6)
                            .name("")
                            .build()))
            .likes(Set.of(1))
            .build();
    private final User user = User.builder()
            .email("")
            .login("")
            .name("")
            .birthday(LocalDate.now())
            .friends(new HashSet<>())
            .build();

    private FilmDaoDbImpl filmDao;
    private UserDaoDbImpl userDao;

    @BeforeEach
    void beforeEach() {
        filmDao = new FilmDaoDbImpl(jdbcTemplate);
        userDao = new UserDaoDbImpl(jdbcTemplate);
        userDao.merge(user);
    }

    @Test
    void testGetAllFilms() {
        filmDao.merge(film1);
        filmDao.merge(film2);
        Map<Integer, Film> expected = Map.of(1, film1, 2, film2);

        Map<Integer, Film> films = filmDao.get();

        assertEquals(2, films.size());
        assertEquals(films.get(1), film1);
        assertEquals(films.get(1).getId(), 1);
        assertEquals(films.get(2), film2);
        assertEquals(films.get(2).getId(), 2);
        assertThat(films).isNotNull().usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testGetFilmById() {
        filmDao.merge(film1);

        Film retrievedFilm = filmDao.get(1);

        assertThat(retrievedFilm).isNotNull().usingRecursiveComparison().isEqualTo(film1);
    }

    @Test
    void testGetFilmByIdNotFound() {
        assertThrows(FilmNotFoundException.class, () -> filmDao.get(1));
    }

    @Test
    void testMergeFilmCreate() {
        Film film = filmDao.merge(film1);

        assertThat(film).isNotNull().usingRecursiveComparison().isEqualTo(film1);
        assertEquals(film.getId(), 1);
    }

    @Test
    void testMergeFilmUpdate() {
        filmDao.merge(film1);
        film1.setName("Updated Film");
        film1.setDescription("Updated Description");
        film1.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1.setDuration(120);
        film1.setMpa(new MPARating(2, ""));
        film1.setGenres(Set.of(new Genre(2, "")));

        Film updatedFilm = filmDao.merge(film1);

        assertThat(updatedFilm).isNotNull().usingRecursiveComparison().isEqualTo(film1);
    }
}