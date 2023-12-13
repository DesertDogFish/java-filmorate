package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.Map;

public interface Storage {
    Map<Integer, AbstractIdModel> get();

    AbstractIdModel get(int id);

    void put(int id, AbstractIdModel value);

    void remove(int id);
}
