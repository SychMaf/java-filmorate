package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film createLike(Integer userId, Integer filmId) {
        Film film = inMemoryFilmStorage.findById(filmId);
        Set<Integer> likes = film.getLike();
        likes.add(userId);
        film.setLike(likes);
        inMemoryFilmStorage.update(film);
        log.info("like created");
        return inMemoryFilmStorage.findById(filmId);
    }

    public Film deleteLike(Integer userId, Integer filmId) {
        Film film = inMemoryFilmStorage.findById(filmId);
        Set<Integer> likes = film.getLike();
        if (!likes.contains(userId)) {
            log.error("user not found in likes");
            throw new NotFoundException("User with this Id didn't like this movie");
        }
        likes.remove(userId);
        film.setLike(likes);
        inMemoryFilmStorage.update(film);
        log.info("like deleted");
        return inMemoryFilmStorage.findById(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> popular = inMemoryFilmStorage.findAll();
        popular.sort(Comparator.comparing(o -> o.getLike().size()));
        Collections.reverse(popular);
        if (count < popular.size()) {
            List<Film> trimToSize = popular.subList(0, count);
            return trimToSize;
        }
        return popular;
    }
}