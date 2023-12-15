package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BasicDao;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class BasicDaoDbImpl<M extends AbstractIdModel> implements BasicDao<M> {
    protected final Map<Integer, M> data = new HashMap<>();
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public BasicDaoDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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