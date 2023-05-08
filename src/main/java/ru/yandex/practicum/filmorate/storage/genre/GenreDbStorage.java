package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "select g.* " +
                "from genre as g;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        findGenre(id);
        String sql = "select g.* " +
                " from genre as g " +
                "where g.genre_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> createGenre(rs), id);
    }

    @Override
    public void createGenreFromFilm(Set<Genre> genres, int filmId) {
        String sql = "insert into film_genre (film_id, genre_id) " +
                "values (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql,
                    filmId,
                    genre.getId());
        }
    }

    @Override
    public void updateGenreFromFilm(Set<Genre> genres, int filmId) {
        deleteGenreFromFilm(filmId);
        createGenreFromFilm(genres, filmId);
    }

    @Override
    public void deleteGenreFromFilm(int filmId) {
        String sql = "delete from film_genre " +
                "where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }


    @Override
    public List<Genre> getGenreByIdFilm(int filmId) {
        String sql = "select g.*" +
                "from film_genre as fg " +
                "join genre as g on fg.genre_id = g.genre_id " +
                "where fg.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createGenre(rs), filmId);
    }

    private Genre createGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }

    private void findGenre(int genreId) {
        String sql = "select g.* " +
                " from genre as g " +
                "where g.genre_id = ?";
        SqlRowSet rsGenre = jdbcTemplate.queryForRowSet(sql,
                genreId);
        if (!rsGenre.next()) {
            throw new NotFoundException("Genre not found");
        }
    }
}
