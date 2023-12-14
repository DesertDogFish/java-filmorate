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
public class BasicDaoDbImpl<M extends AbstractIdModel> implements BasicDao {
    protected final Map<Integer, M> data = new HashMap<>();
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public BasicDaoDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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