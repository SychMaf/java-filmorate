package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film findById(Integer filmId);

    boolean createLike(int userId, int filmId);

    boolean deleteLike(int userId, int filmId);

    List<Film> getPopularFilms(Integer count);
}