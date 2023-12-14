package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MPARating extends AbstractIdModel {
    private String name;

    public MPARating(int id, String name) {
        super.setId(id);
        this.name = name;
    }
}