package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.Message.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {

    private final FilmStorage storage;
    private final UserStorage userStorage;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("filmStorage") FilmStorage storage, UserStorage userStorage, MPAStorage mpaStorage, GenreStorage genreStorage) {
        setStorage(storage);
        this.storage = storage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film create(Film body) {
        enrichMpaAndGenre(body);
        return super.create(body);
    }

    @Override
    public Film put(Film body) {
        enrichMpaAndGenre(body);
        return super.put(body);
    }

    private void enrichMpaAndGenre(Film body) {
        body.setMpa((MPARating) mpaStorage.get(body.getMpa().getId()));
        Set<Genre> genresEnriched = new HashSet<>();
        for (Genre genre : body.getGenres())
            genresEnriched.add((Genre) genreStorage.get(genre.getId()));
        body.getGenres().removeAll(body.getGenres());
        genresEnriched = genresEnriched.stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        body.getGenres().addAll(genresEnriched);
    }

    public Film addLike(int id, int userId) {
        Film film = (Film) storage.get(id);
        if (userStorage.get(userId) == null) {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        if (film != null) film.getLikes().add(userId);
        else {
            log.warn(FILM_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(FILM_NOT_FOUND_MESSAGE);
        }
        return film;
    }

    public Film removeLike(int id, int userId) {
        Film film = (Film) storage.get(id);
        if (userStorage.get(userId) == null) {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        if (film != null) film.getLikes().remove(userId);
        else {
            log.warn(FILM_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(FILM_NOT_FOUND_MESSAGE);
        }
        return film;
    }

    public List<Film> getTop(int count) {
        return storage.get().values().stream().map(film -> (Film) film).sorted(Comparator.comparingInt((Film film) -> (film).getLikes().size()).reversed()).limit(count).collect(Collectors.toList());
    }
}
