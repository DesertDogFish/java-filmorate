package ru.yandex.practicum.filmorate.dao.dbimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.BasicDao;
import ru.yandex.practicum.filmorate.model.AbstractIdModel;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public abstract class AbstractDaoDbImpl<M extends AbstractIdModel> implements BasicDao<M> {
    protected final Map<Integer, M> data = new HashMap<>();
    protected JdbcTemplate jdbcTemplate;

    public AbstractDaoDbImpl(JdbcTemplate jdbcTemplate) {
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
    public M merge(M value) {
        data.put(value.getId(), value);
        return value;
    }

    @Override
    public void remove(int id) {
        data.remove(id);
    }
}