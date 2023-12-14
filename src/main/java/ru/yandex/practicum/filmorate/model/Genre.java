package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Genre extends AbstractIdModel {
    private String name;

    public Genre(int id, String name) {
        super.setId(id);
        this.name = name;
    }
}