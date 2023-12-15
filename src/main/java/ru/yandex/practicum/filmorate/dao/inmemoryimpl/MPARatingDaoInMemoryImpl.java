package ru.yandex.practicum.filmorate.dao.inmemoryimpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MPARatingDao;
import ru.yandex.practicum.filmorate.model.MPARating;

@Component("mpaStorage")
public class MPARatingDaoInMemoryImpl extends AbstractDaoInMemoryImpl<MPARating> implements MPARatingDao {

    public MPARatingDaoInMemoryImpl() {
        data.put(1, new MPARating(1, "G"));
        data.put(2, new MPARating(2, "PG"));
        data.put(3, new MPARating(3, "PG-13"));
        data.put(4, new MPARating(4, "R"));
        data.put(5, new MPARating(5, "NC-17"));
    }
}