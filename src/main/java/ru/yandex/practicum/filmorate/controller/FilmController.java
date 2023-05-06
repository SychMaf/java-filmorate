package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@NotNull @Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@NotNull @Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.findFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable int id,
                           @PathVariable int userId) {
        return filmService.createLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean removeLike(@PathVariable int id,
                              @PathVariable int userId) {
        return filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}