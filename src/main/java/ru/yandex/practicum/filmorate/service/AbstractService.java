package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.BasicDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.Message.NOT_FOUND_MESSAGE;

@Service
@Slf4j
public abstract class AbstractService<M extends AbstractIdModel> {

    protected BasicDao<M> dao;
    protected int counter = 1;

    protected void setDao(BasicDao<M> dao) {
        this.dao = dao;
    }

    public Collection<M> get() {
        log.debug("Текущее количество записей: {}", dao.get().size());
        return dao.get().values().stream().map(this::enrichFields).collect(Collectors.toList());
    }

    public M get(int id) {
        log.debug("Получаем сущность по id: {}", id);
        M model = dao.get(id);
        if (model == null) {
            log.warn(NOT_FOUND_MESSAGE);
            throw new ValidationException(NOT_FOUND_MESSAGE);
        }
        return enrichFields(model);
    }

    public M create(M body) {
        log.debug("Добавляем: {}", body);
        body.setId(counter++);
        dao.put(body.getId(), enrichFields(body));
        log.debug("Добавлено: {}", body);
        return body;
    }

    public M put(M body) {
        log.debug("Обновляем: {}", body);
        if (!dao.get().containsKey(body.getId())) {
            log.warn(NOT_FOUND_MESSAGE);
            throw new ValidationException(NOT_FOUND_MESSAGE);
        }
        dao.put(body.getId(), body);
        log.debug("Обновлено: {}", enrichFields(body));
        return body;
    }

    protected M enrichFields(M body) {
        return body;
    }
}