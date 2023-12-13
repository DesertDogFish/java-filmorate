package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.Message.FILM_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {

    private final FilmStorage storage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmStorage") FilmStorage storage, UserStorage userStorage) {
        setStorage(storage);
        this.storage = storage;
        this.userStorage = userStorage;
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
