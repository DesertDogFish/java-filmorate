package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAbstractStorage<M extends AbstractIdModel> implements FilmStorage {
    protected final Map<Integer, M> data = new HashMap<>();

    @Override
    public AbstractIdModel get(int id) {
        return data.get(id);
    }

    @Override
    public Map<Integer, AbstractIdModel> get() {
        return (Map<Integer, AbstractIdModel>) data;
    }

    @Override
    public void put(int id, AbstractIdModel value) {
        data.put(id, (M) value);
    }

    @Override
    public void remove(int id) {
        data.remove(id);
    }
}