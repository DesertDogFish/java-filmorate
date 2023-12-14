package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Genre extends AbstractIdModel {
    private String name;

    public Genre(int id, String name) {
        super.setId(id);
        this.name = name;
    }
}