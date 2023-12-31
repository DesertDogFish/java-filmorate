package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.Message.USER_NOT_FOUND_MESSAGE;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    private final UserDao userDao;

    public UserService(@Qualifier("userDbStorage") UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    protected User enrichFields(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty())
            user.setName(user.getLogin());
        return user;
    }

    public User addFriend(int id, int friendId) {
        log.debug("Добавляем друзей для: {} {}", id, friendId);
        User user = userDao.get(id);
        User friend = userDao.get(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        userDao.merge(user);
        return user;
    }

    public User removeFriend(int id, int friendId) {
        log.debug("Удаляем друзей для: {} {}", id, friendId);
        User user = userDao.get(id);
        User friend = userDao.get(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        userDao.merge(user);
        return user;
    }

    public List<User> getFriends(int id) {
        log.debug("Ищем друзей для: {}", id);
        List<User> friends = new ArrayList<>();
        User user = userDao.get(id);
        if (user != null) {
            friends = user.getFriends().stream().map(userDao::get).collect(Collectors.toList());
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.debug("Ищем общих друзей для: {} {}", id, otherId);
        List<User> commonFriends = new ArrayList<>();
        User user = userDao.get(id);
        User otherUser = userDao.get(otherId);
        if (user != null && otherUser != null) {
            Set<Integer> commonIds = new HashSet<>(user.getFriends());
            commonIds.retainAll(otherUser.getFriends());
            commonFriends = commonIds.stream().map(userDao::get).collect(Collectors.toList());
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return commonFriends;
    }
}
