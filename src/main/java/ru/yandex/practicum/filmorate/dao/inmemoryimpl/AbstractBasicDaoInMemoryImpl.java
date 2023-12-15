package ru.yandex.practicum.filmorate.dao.inmemoryimpl;

import ru.yandex.practicum.filmorate.dao.BasicDao;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.HashMap;
import java.util.Map;

public class AbstractBasicDaoInMemoryImpl<M extends AbstractIdModel> implements BasicDao<M> {
    protected final Map<Integer, M> data = new HashMap<>();

    @Override
    public M get(int id) {
        return data.get(id);
    }

    @Override
    public Map<Integer, M> get() {
        return data;
    }

    @Override
    public void put(int id, M value) {
        data.put(id, value);
    }

    @Override
    public void remove(int id) {
        data.remove(id);
    }
}