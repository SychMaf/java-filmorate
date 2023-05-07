package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film findById(Integer filmId) throws NotFoundException {
        findFilm(filmId);
        String sql = "select f.*, " +
                "m.name " +
                "from films as f " +
                "join mpa as m on f.mpa_id = m.mpa_id " +
                "where f.film_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> createFilm(rs, genreStorage), filmId);
    }

    @Override
    public List<Film> findAll() {
        String sql = "select f.*, " +
                "m.name " +
                "from films as f " +
                "join mpa as m on f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createFilm(rs, genreStorage));
    }

    @Override
    public Film create(Film film) {
        String sql = "insert into films (name, description, mpa_id, releasedate, duration) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getMpa().getId());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        if (!film.getGenres().isEmpty()) {
            genreStorage.createGenreFromFilm(new LinkedHashSet<>(film.getGenres()), film.getId());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        int filmId = film.getId();
        findFilm(filmId);
        String sql = "update films " +
                "set name = ?, description = ?, mpa_id = ?, releasedate = ?, duration = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration(),
                filmId);
        genreStorage.updateGenreFromFilm(new LinkedHashSet<>(film.getGenres()), filmId);
        return findById(filmId);
    }

    @Override
    public boolean createLike(int userId, int filmId) {
        findFilm(filmId);
        userStorage.findById(userId);
        String sql = "merge into likes key(film_id, user_id) " +
                "values (?, ?)";
        return jdbcTemplate.update(sql, filmId, userId) > 0;
    }

    @Override
    public boolean deleteLike(int userId, int filmId) {
        findFilm(filmId);
        userStorage.findById(userId);
        String sql = "delete from likes " +
                "where user_id = ? and film_id = ?";
        return jdbcTemplate.update(sql, userId, filmId) > 0;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = "select f.*, " +
                " count (l.user_id) as count_likes, " +
                "m.name " +
                "from films as f " +
                "left outer join likes as l on f.film_id = l.film_id " +
                "left outer join mpa as m on f.mpa_id = m.mpa_id " +
                "group by f.name " +
                "order by count (l.user_id) desc " +
                "limit ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createFilm(rs, genreStorage), count);
    }

    private Film createFilm(ResultSet rs, GenreStorage genreStorage) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate creationDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa.name"))
                .build();
        List<Genre> genres = genreStorage.getGenreByIdFilm(id);
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(creationDate)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    private void findFilm(int filmId) {
        String sql = "select film_id from films as f " +
                "where film_id = ?";
        SqlRowSet rsFilm = jdbcTemplate.queryForRowSet(sql,
                filmId);
        if (!rsFilm.next()) {
            throw new NotFoundException("Film not found");
        }
    }
}
