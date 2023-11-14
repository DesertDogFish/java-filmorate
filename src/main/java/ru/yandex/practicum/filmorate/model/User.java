package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractIdModel {
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    @NotEmpty(message = "логин не может быть пустым")
    @NoSpaces(message = "логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
}
