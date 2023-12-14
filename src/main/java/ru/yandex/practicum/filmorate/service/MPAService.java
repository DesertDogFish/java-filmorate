package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
public class MPAService extends AbstractService<MPARating> {


    private final MPAStorage storage;

    public MPAService(@Qualifier("mpaStorage") MPAStorage storage) {
        setStorage(storage);
        this.storage = storage;
    }

    @Override
    public MPARating create(MPARating body) {
        //TODO: deal with ex
        log.debug("??????????????????????????????????????");
        throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
    }

    @Override
    public MPARating put(MPARating body) {
        //TODO: deal with ex
        log.debug("??????????????????????????????????????");
        throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
    }
}
