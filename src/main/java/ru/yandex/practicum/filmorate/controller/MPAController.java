package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPAService;

@RestController
@RequestMapping("/mpa")
public class MPAController extends AbstractController<MPARating> {

    protected final MPAService service;

    public MPAController(MPAService service) {
        super(service);
        this.service = service;
    }
}