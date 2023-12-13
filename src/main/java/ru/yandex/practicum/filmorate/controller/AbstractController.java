package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;
import ru.yandex.practicum.filmorate.service.AbstractService;

import javax.validation.Valid;
import java.util.Collection;

public abstract class AbstractController<M extends AbstractIdModel> {
    protected final AbstractService<M> service;

    public AbstractController(AbstractService<M> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public M get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping
    public Collection<M> findAll() {
        return service.findAll();
    }

    @PostMapping
    public M create(@Valid @RequestBody M body) {
        return service.create(body);
    }

    @PutMapping
    public M put(@Valid @RequestBody M body) {
        return service.put(body);
    }
}
