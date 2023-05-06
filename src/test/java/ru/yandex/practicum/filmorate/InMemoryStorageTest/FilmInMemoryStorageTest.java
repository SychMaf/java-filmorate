package ru.yandex.practicum.filmorate.InMemoryStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmInMemoryStorageTest {
    @Qualifier("InMemoryFilmStorage") private final FilmStorage filmStorage;

    @Test
    @Order(1)
    void createFilmTest() {
        Film filmTest = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .id(1)
                .build();
        filmStorage.create(filmTest);
        assertEquals(filmStorage.findAll().get(0), filmTest);
    }

    @Test
    @Order(2)
    void updateFilmTest() {
        Film filmTestUpdate = Film.builder()
                .name("filmUpdate")
                .description("descriptionUpdate")
                .releaseDate(LocalDate.of(1977, 3, 27))
                .duration(1000)
                .id(1)
                .build();
        filmStorage.update(filmTestUpdate);
        assertEquals(filmStorage.findById(1), filmTestUpdate);
    }
}