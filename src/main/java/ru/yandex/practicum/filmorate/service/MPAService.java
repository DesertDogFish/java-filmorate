package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnacceptedMethodException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.dao.MPARatingDao;

import static ru.yandex.practicum.filmorate.service.Message.UACCEPTED_METHOD_MESSAGE;

@Slf4j
@Service
public class MPAService extends AbstractService<MPARating> {

    public MPAService(@Qualifier("mpaDbStorage") MPARatingDao mpaRatingDao) {
        setDao(mpaRatingDao);
    }

    @Override
    public MPARating create(MPARating body) {
        log.warn(UACCEPTED_METHOD_MESSAGE);
        throw new UnacceptedMethodException("create");
    }

    @Override
    public MPARating put(MPARating body) {
        log.warn(UACCEPTED_METHOD_MESSAGE);
        throw new UnacceptedMethodException("put");
    }
}
