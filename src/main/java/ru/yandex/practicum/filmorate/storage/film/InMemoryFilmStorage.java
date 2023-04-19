package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    public List<Film> findAll() {
        log.info("Return film list");
        return new ArrayList<>(films.values());
    }

    public Film findById(Integer id) {
        if (!films.containsKey(id)) {
            log.error("Film not found");
            throw new NotFoundException("Film not found");
        }
        return films.get(id);
    }

    public Film create(Film film) {
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

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film not found");
            throw new NotFoundException("Film not found");
        }
        films.put(film.getId(), film);
        log.info("Film successfully updated");
        return film;
    }
}