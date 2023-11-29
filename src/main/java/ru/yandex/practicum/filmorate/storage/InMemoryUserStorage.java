package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component("userStorage")
public class InMemoryUserStorage extends InMemoryAbstractStorage<User> implements UserStorage {
}
