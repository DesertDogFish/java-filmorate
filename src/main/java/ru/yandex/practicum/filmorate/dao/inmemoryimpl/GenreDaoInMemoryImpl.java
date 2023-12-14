package ru.yandex.practicum.filmorate.dao.inmemoryimpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

@Component("genreStorage")
public class GenreDaoInMemoryImpl extends AbstractBasicDaoInMemoryImpl<Genre> implements GenreDao {

    public GenreDaoInMemoryImpl() {
        data.put(1, new Genre(1, "Комедия"));
        data.put(2, new Genre(2, "Драма"));
        data.put(3, new Genre(3, "Мультфильм"));
        data.put(4, new Genre(4, "Триллер"));
        data.put(5, new Genre(5, "Документальный"));
        data.put(6, new Genre(6, "Боевик"));
    }
}