package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.MaxLength;
import ru.yandex.practicum.filmorate.validation.OlderThan;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Film extends AbstractIdModel {
    @NotEmpty(message = "название не может быть пустым")
    private String name;
    @MaxLength(value = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @OlderThan(value = "1895-12-27", message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private int duration;
}