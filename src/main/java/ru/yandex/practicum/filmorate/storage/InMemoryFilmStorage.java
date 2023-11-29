package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component("filmStorage")
public class InMemoryFilmStorage extends InMemoryAbstractStorage<Film> implements FilmStorage {
}
