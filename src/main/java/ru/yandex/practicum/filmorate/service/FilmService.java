package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

    public Film createLike(Integer userId, Integer filmId) {
        Film film = filmStorage.findById(filmId);
        film.addLike(userId);
        filmStorage.update(film);
        log.info("like created");
        return filmStorage.findById(filmId);
    }

    public Film deleteLike(Integer userId, Integer filmId) {
        Film film = filmStorage.findById(filmId);
        if (!film.getLikes().contains(userId)) {
            log.error("user not found in likes");
            throw new NotFoundException("User with this Id didn't like this movie");
        }
        film.deleteLike(userId);
        filmStorage.update(film);
        log.info("like deleted");
        return filmStorage.findById(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(o -> o.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}