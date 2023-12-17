package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractIdModel {
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    @NotEmpty(message = "логин не может быть пустым")
    @NoSpaces(message = "логин не может содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}