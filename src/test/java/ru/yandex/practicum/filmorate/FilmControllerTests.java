package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmControllerTests {

    @Test
    void createFilmTest() {
        Film filmTest = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .id(1)
                .build();
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        inMemoryFilmStorage.create(filmTest);
        assertEquals(inMemoryFilmStorage.findAll().get(0), filmTest);
    }

    @Test
    void updateFilmTest() {
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
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        inMemoryFilmStorage.create(filmTest);
        inMemoryFilmStorage.update(filmTestUpdate);
        assertEquals(inMemoryFilmStorage.findAll().get(0), filmTestUpdate);
    }

    @Test
    void createAndDeleteLikeTest() {
        Film filmTest = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .id(1)
                .build();
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        inMemoryFilmStorage.create(filmTest);
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.create(userTest);
        FilmService filmService = new FilmService(inMemoryFilmStorage);
        filmService.createLike(1, 1);
        assertEquals(filmService.getPopularFilms(10).get(0).getLikes().size(), 1);
        filmService.deleteLike(1, 1);
        assertEquals(filmService.getPopularFilms(10).get(0).getLikes().size(), 0);
    }
}