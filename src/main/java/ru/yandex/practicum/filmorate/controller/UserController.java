package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @Override
    protected void setFields(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty())
            user.setName(user.getLogin());
    }
}