package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.Map;

public interface BasicDao<M extends AbstractIdModel> {
    Map<Integer, M> get();

    M get(int id);

    void put(int id, M value);

    void remove(int id);
}
