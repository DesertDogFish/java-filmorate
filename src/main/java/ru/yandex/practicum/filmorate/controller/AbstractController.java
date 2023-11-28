package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractController<T extends AbstractIdModel> {
    private int counter = 1;
    private final Map<Integer, T> data = new HashMap<>();

    @GetMapping
    public Collection<T> findAll() {
        log.debug("Текущее количество записей: {}", data.size());
        return data.values();
    }

    @PostMapping
    public T create(@Valid @RequestBody T body) {
        setFields(body);
        log.debug("Добавляем: {}", body);
        body.setId(counter++);
        data.put(body.getId(), body);
        return body;
    }

    @PutMapping
    public T put(@Valid @RequestBody T body) {
        setFields(body);
        log.debug("Обновляем: {}", body);
        if (!data.containsKey(body.getId())) {
            String msg = "Фильм не найден";
            log.warn(msg);
            throw new ValidationException(msg);
        }
        data.put(body.getId(), body);
        return body;
    }

    protected void setFields(T body) {
    }
}
