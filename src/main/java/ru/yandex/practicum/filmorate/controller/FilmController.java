package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@NotNull @Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@NotNull @Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id,
                        @PathVariable int userId) {
        return filmService.createLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id,
                           @PathVariable int userId) {
        return filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}