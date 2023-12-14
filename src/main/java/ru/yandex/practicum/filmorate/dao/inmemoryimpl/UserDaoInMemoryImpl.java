package ru.yandex.practicum.filmorate.dao.inmemoryimpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

@Component("userStorage")
public class UserDaoInMemoryImpl extends AbstractBasicDaoInMemoryImpl<User> implements UserDao {
}
