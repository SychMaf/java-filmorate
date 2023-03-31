package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmControllerTests {

    @Test
    void CreateFilmTest() {
        Film filmTest = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .id(1)
                .build();
        FilmController filmController = new FilmController();
        filmController.create(filmTest);
        assertEquals(filmController.findAll().get(0), filmTest);
    }

    @Test
    void UpdateFilmTest() {
        Film filmTest = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .id(1)
                .build();
        Film filmTestUpdate = Film.builder()
                .name("filmUpdate")
                .description("descriptionUpdate")
                .releaseDate(LocalDate.of(1977, 3, 27))
                .duration(1000)
                .id(1)
                .build();
        FilmController filmController = new FilmController();
        filmController.create(filmTest);
        filmController.update(filmTestUpdate);
        assertEquals(filmController.findAll().get(0), filmTestUpdate);
    }
}