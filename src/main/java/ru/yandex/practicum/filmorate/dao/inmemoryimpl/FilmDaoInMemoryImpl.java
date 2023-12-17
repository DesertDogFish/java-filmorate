package ru.yandex.practicum.filmorate.dao.inmemoryimpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

@Component("filmStorage")
public class FilmDaoInMemoryImpl extends AbstractDaoInMemoryImpl<Film> implements FilmDao {
}
