package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.PositiveOrZero;

@Data
@SuperBuilder
@RequiredArgsConstructor
public abstract class AbstractIdModel {
    @PositiveOrZero
    private int id;
}