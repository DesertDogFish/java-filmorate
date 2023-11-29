package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    private static final String URL = "/films";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FilmService filmService;
    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setId(1);
        film.setName("Film1");
        film.setDescription("Description1");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
    }

    @Test
    public void testFindAllWhenFilmsExistThenReturnAllFilms() throws Exception {

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film)));

        Film film2 = new Film();
        film2.setId(2);
        film2.setName("Film2");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(2001, 1, 1));
        film2.setDuration(130);

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film2)));

        Collection<Film> films = Arrays.asList(film, film2);

        Mockito.when(filmService.findAll()).thenReturn(films);

        mockMvc.perform(get(URL)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(films)));
        Mockito.verify(filmService, Mockito.times(1)).findAll();
    }

    @Test
    public void testCreateWhenValidFilmThenCreateFilm() throws Exception {
        Mockito.when(filmService.create(film)).thenReturn(film);
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(film)));
        Mockito.verify(filmService, Mockito.times(1)).create(film);
    }

    @Test
    public void testCreateWhenInvalidFilmThenBadRequest_EmptyName() throws Exception {
        film.setName("");
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenInvalidFilmThenBadRequest_MaxDescriptionReached() throws Exception {
        film.setDescription("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" + "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890A");
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenInvalidFilmThenBadRequest_TooOld() throws Exception {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateWhenInvalidFilmThenBadRequest_NegativeDuration() throws Exception {
        film.setDuration(-1);
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().isBadRequest());
    }

    @Test
    public void testPutWhenValidFilmThenUpdateFilm() throws Exception {
        film.setName("Updated Film");
        film.setDescription("Updated Description");
        film.setReleaseDate(LocalDate.of(2001, 1, 1));
        film.setDuration(220);

        Mockito.when(filmService.put(film)).thenReturn(film);

        mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(film)));
        Mockito.verify(filmService, Mockito.times(1)).put(film);
    }

    @Test
    public void testPutWhenInvalidFilmThenServerError() throws Exception {
        film.setId(777);
        Mockito.when(filmService.put(film)).thenThrow(new ValidationException("Film not found"));
        mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(film))).andExpect(status().is4xxClientError());
    }

    @Test
    public void testAddLikeWhenLikeAddedThenReturnFilmWithLike() throws Exception {
        Film filmWithLike = new Film();
        filmWithLike.setId(1);
        filmWithLike.setName("Film1");
        filmWithLike.setDescription("Description1");
        filmWithLike.setReleaseDate(LocalDate.of(2000, 1, 1));
        filmWithLike.setDuration(120);
        filmWithLike.getLikes().add(10);

        Mockito.when(filmService.addLike(eq(1), eq(10))).thenReturn(filmWithLike);

        mockMvc.perform(put(URL + "/{id}/like/{userId}", 1, 10).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(filmWithLike)));
    }

    @Test
    public void testGetPopularFilmsWhenValidCountThenReturnPopularFilms() throws Exception {
        List<Film> popularFilms = Collections.singletonList(film);

        Mockito.when(filmService.getTop(anyInt())).thenReturn(popularFilms);

        mockMvc.perform(get(URL + "/popular").param("count", "1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(popularFilms)));
    }
}
