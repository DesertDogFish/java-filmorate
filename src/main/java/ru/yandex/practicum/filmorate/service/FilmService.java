package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MPARatingDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.Message.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final MPARatingDao mpaRatingDao;
    private final GenreDao genreDao;

    public FilmService(@Qualifier("filmDbStorage") FilmDao filmDao,
                       @Qualifier("userDbStorage") UserDao userDao,
                       @Qualifier("mpaDbStorage") MPARatingDao mpaRatingDao,
                       @Qualifier("genreDbStorage") GenreDao genreDao) {
        super(filmDao);
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.mpaRatingDao = mpaRatingDao;
        this.genreDao = genreDao;
    }

    @Override
    protected Film enrichFields(Film body) {
        log.debug("Добавляем имена значений MPA и Genre для: {}", body);
        body.setMpa(mpaRatingDao.get(body.getMpa().getId()));
        Set<Genre> genresEnriched = new HashSet<>();
        for (Genre genre : body.getGenres())
            genresEnriched.add(genreDao.get(genre.getId()));
        body.getGenres().removeAll(body.getGenres());
        genresEnriched = genresEnriched.stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        body.getGenres().addAll(genresEnriched);
        log.debug("После добавления значений MPA и Genre: {}", body);
        return body;
    }

    public Film addLike(int id, int userId) {
        log.debug("Добавляем лайк для фильма {} юзера {}", id, userId);
        Film film = filmDao.get(id);
        if (userDao.get(userId) == null) {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        if (film != null) film.getLikes().add(userId);
        else {
            log.warn(FILM_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(FILM_NOT_FOUND_MESSAGE);
        }
        filmDao.merge(film);
        log.debug("Добавлен лайк для: {}", film);
        return enrichFields(film);
    }

    public Film removeLike(int id, int userId) {
        log.debug("Удаляем лайк для фильма {} юзера {}", id, userId);
        Film film = filmDao.get(id);
        if (userDao.get(userId) == null) {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        if (film != null) film.getLikes().remove(userId);
        else {
            log.warn(FILM_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(FILM_NOT_FOUND_MESSAGE);
        }
        filmDao.merge(film);
        log.debug("Удален лайк для: {}", film);
        return enrichFields(film);
    }

    public List<Film> getTop(int count) {
        log.debug("Получаем топ {} лучших фильмов", count);
        List<Film> films = filmDao.get().values().stream()
                .map(this::enrichFields)
                .sorted(Comparator.comparingInt((Film film) -> (film).getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Топ лучших фильмов {} ", films);
        return films;
    }
}
