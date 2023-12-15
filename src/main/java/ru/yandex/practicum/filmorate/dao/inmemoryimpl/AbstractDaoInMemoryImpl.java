package ru.yandex.practicum.filmorate.dao.inmemoryimpl;

import ru.yandex.practicum.filmorate.dao.BasicDao;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDaoInMemoryImpl<M extends AbstractIdModel> implements BasicDao<M> {
    protected final Map<Integer, M> data = new HashMap<>();
    protected int counter = 1;

    @Override
    public M get(int id) {
        return data.get(id);
    }

    @Override
    public Map<Integer, M> get() {
        return data;
    }

    @Override
    public M merge(M value) {
        if (value.getId() == 0)
            value.setId(counter++);
        data.put(value.getId(), value);
        return value;
    }

    @Override
    public void remove(int id) {
        data.remove(id);
    }
}