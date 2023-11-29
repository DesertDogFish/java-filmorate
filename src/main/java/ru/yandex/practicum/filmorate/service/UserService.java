package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    private final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";

    private final UserStorage storage;

    public UserService(@Qualifier("userStorage") UserStorage storage) {
        setStorage(storage);
        this.storage = storage;
    }

    @Override
    protected void setFields(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty())
            user.setName(user.getLogin());
    }

    public User addFriend(int id, int friendId) {
        log.debug("Добавляем друзей для: {} {}", id, friendId);
        User user = (User) storage.get(id);
        User friend = (User) storage.get(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            friend.getFriends().add(id);
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return user;
    }

    public User removeFriend(int id, int friendId) {
        log.debug("Удаляем друзей для: {} {}", id, friendId);
        User user = (User) storage.get(id);
        User friend = (User) storage.get(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return user;
    }

    public List<User> getFriends(int id) {
        log.debug("Ищем друзей для: {}", id);
        List<User> friends = new ArrayList<>();
        User user = (User) storage.get(id);
        if (user != null) {
            friends = user.getFriends().stream().map(friendId -> (User) storage.get(friendId)).collect(Collectors.toList());
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.debug("Ищем общих друзей для: {} {}", id, otherId);
        List<User> commonFriends = new ArrayList<>();
        User user = (User) storage.get(id);
        User otherUser = (User) storage.get(otherId);
        if (user != null && otherUser != null) {
            Set<Integer> commonIds = new HashSet<>(user.getFriends());
            commonIds.retainAll(otherUser.getFriends());
            commonFriends = commonIds.stream().map(friendId -> (User) storage.get(friendId)).collect(Collectors.toList());
        } else {
            log.warn(USER_NOT_FOUND_MESSAGE);
            throw new FilmNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        return commonFriends;
    }
}
