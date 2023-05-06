package ru.yandex.practicum.filmorate.storageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreStorage genreStorage;

    @Test
    public void testFindMpaById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenreById(1));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void testGetNotFoundMpa() {
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> genreStorage.getGenreById(9999));
        assertEquals("Genre not found", ex.getMessage());
    }

    @Test
    public void testGenreGetAll() {
        Optional<List<Genre>> genreOptional = Optional.ofNullable(genreStorage.getAllGenres());
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre.get(0)).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "Комедия")
                )
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.get(4)).hasFieldOrPropertyWithValue("id", 5)
                                .hasFieldOrPropertyWithValue("name", "Документальный")
                )
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.size()).isEqualTo(6)
                );
    }
}
