package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnacceptedMethodException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.GenreDao;

import static ru.yandex.practicum.filmorate.service.Message.UACCEPTED_METHOD_MESSAGE;

@Slf4j
@Service
public class GenreService extends AbstractService<Genre> {

    public GenreService(@Qualifier("genreDbStorage") GenreDao genreDao) {
        super(genreDao);
    }

    @Override
    public Genre create(Genre body) {
        log.warn(UACCEPTED_METHOD_MESSAGE);
        throw new UnacceptedMethodException("create");
    }

    @Override
    public Genre put(Genre body) {
        log.warn(UACCEPTED_METHOD_MESSAGE);
        throw new UnacceptedMethodException("create");
    }
}
