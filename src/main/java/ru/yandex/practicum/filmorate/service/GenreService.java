package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
public class GenreService extends AbstractService<Genre> {


    private final GenreStorage storage;

    public GenreService(@Qualifier("genreStorage") GenreStorage storage) {
        setStorage(storage);
        this.storage = storage;
    }

    @Override
    public Genre create(Genre body) {
        //TODO: deal with ex
        log.debug("??????????????????????????????????????");
        throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
    }

    @Override
    public Genre put(Genre body) {
        //TODO: deal with ex
        log.debug("??????????????????????????????????????");
        throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
    }
}
