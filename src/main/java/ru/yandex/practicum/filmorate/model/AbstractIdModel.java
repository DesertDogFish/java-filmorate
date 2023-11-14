package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

@Data
public abstract class AbstractIdModel {
    @PositiveOrZero
    private int id;
}