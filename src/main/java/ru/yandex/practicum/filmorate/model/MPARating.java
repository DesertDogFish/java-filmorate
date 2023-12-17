package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MPARating extends AbstractIdModel {
    private String name;

    public MPARating(int id, String name) {
        super();
        super.setId(id);
        this.name = name;
    }
}