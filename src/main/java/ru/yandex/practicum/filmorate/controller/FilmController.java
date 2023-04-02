package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

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