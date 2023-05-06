package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    void createGenreFromFilm(Set<Genre> genres, int filmId);

    void updateGenreFromFilm(Set<Genre> genres, int filmId);

    void deleteGenreFromFilm(int filmId);

    List<Genre> getGenreByIdFilm(int filmId);

    Genre getGenreById(int id);

    List<Genre> getAllGenres();
}
