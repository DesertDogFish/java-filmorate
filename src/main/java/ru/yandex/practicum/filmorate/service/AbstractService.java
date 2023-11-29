package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.service.Message.NOT_FOUND_MESSAGE;

@Service
@Slf4j
public abstract class AbstractService<M extends AbstractIdModel> {

    protected Storage storage;
    private int counter = 1;

    protected void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Collection<M> findAll() {
        log.debug("Текущее количество записей: {}", storage.get().size());
        return (Collection<M>) storage.get().values();
    }

    public M get(int id) {
        log.debug("Получаем сущность по id: {}", id);
        M model = (M) storage.get(id);
        if (model == null) {
            log.warn(NOT_FOUND_MESSAGE);
            throw new ValidationException(NOT_FOUND_MESSAGE);
        }
        return model;
    }

    public M create(M body) {
        log.debug("Добавляем: {}", body);
        setFields(body);
        body.setId(counter++);
        storage.put(body.getId(), body);
        return body;
    }

    public M put(M body) {
        log.debug("Обновляем: {}", body);
        setFields(body);
        if (!storage.get().containsKey(body.getId())) {
            log.warn(NOT_FOUND_MESSAGE);
            throw new ValidationException(NOT_FOUND_MESSAGE);
        }
        storage.put(body.getId(), body);
        return body;
    }

    protected void setFields(M body) {
    }
}