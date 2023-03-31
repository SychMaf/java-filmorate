package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private int filmId = 0;

    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> findAll() {
        log.info("Return film list");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@NotNull @Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Film already exist");
            throw new ValidationException("Film already exist");
        }
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Film successfully created");
        return film;
    }

    @PutMapping
    public Film update(@NotNull @Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film not found");
            throw new ValidationException("Film not found");
        }
        films.put(film.getId(), film);
        log.info("Film successfully updated");
        return film;
    }
}