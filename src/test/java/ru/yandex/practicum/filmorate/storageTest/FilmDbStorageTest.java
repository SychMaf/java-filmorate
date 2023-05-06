package ru.yandex.practicum.filmorate.storageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Test
    @Order(1)
    public void createFilmTest() {
        Film filmTest = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .genres(List.of())
                .mpa(Mpa.builder()
                        .id(1)
                        .build())
                .build();
        filmStorage.create(filmTest);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("mpa", Mpa.builder()
                                        .id(1)
                                        .name("G")
                                        .build())
                                .hasFieldOrPropertyWithValue("name", "film")
                                .hasFieldOrPropertyWithValue("description", "description")
                                .hasFieldOrPropertyWithValue("duration", 100)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1967, 3, 25))
                );
    }

    @Test
    @Order(2)
    public void updateFilmTest() {
        Film filmTestUpdate = Film.builder()
                .name("update film")
                .description("update description")
                .releaseDate(LocalDate.of(1977, 3, 25))
                .duration(1000)
                .genres(List.of())
                .mpa(Mpa.builder()
                        .id(2)
                        .build())
                .id(1)
                .build();
        filmStorage.update(filmTestUpdate);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("mpa", Mpa.builder()
                                        .id(2)
                                        .name("PG")
                                        .build())
                                .hasFieldOrPropertyWithValue("name", "update film")
                                .hasFieldOrPropertyWithValue("description", "update description")
                                .hasFieldOrPropertyWithValue("duration", 1000)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1977, 3, 25))
                );
    }

    @Test
    @Order(3)
    public void nonExistFilmUpdateTest() {
        Film filmTestUpdate = Film.builder()
                .name("update film")
                .description("update description")
                .releaseDate(LocalDate.of(1977, 3, 25))
                .duration(1000)
                .genres(List.of())
                .mpa(Mpa.builder()
                        .id(2)
                        .build())
                .id(9999)
                .build();
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> filmStorage.update(filmTestUpdate));
        assertEquals("Film not found", ex.getMessage());
    }

    @Test
    @Order(4)
    public void getAllFilmTest() {
        Film filmTestTwo = Film.builder()
                .name("film second call")
                .description("description two")
                .releaseDate(LocalDate.of(1999, 3, 25))
                .duration(500)
                .genres(List.of())
                .mpa(Mpa.builder()
                        .id(3)
                        .build())
                .build();
        Film anotherFilmTest = Film.builder()
                .name("film third")
                .description("description 3")
                .releaseDate(LocalDate.of(1997, 3, 25))
                .duration(750)
                .genres(List.of(Genre.builder()
                        .id(1)
                        .build()))
                .mpa(Mpa.builder()
                        .id(4)
                        .build())
                .build();
        filmStorage.create(filmTestTwo);
        filmStorage.create(anotherFilmTest);
        Optional<List<Film>> optionalFilmList = Optional.ofNullable(filmStorage.findAll());
        assertThat(optionalFilmList)
                .isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("mpa", Mpa.builder()
                                        .id(2)
                                        .name("PG")
                                        .build())
                                .hasFieldOrPropertyWithValue("name", "update film")
                                .hasFieldOrPropertyWithValue("description", "update description")
                                .hasFieldOrPropertyWithValue("duration", 1000)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1977, 3,25))
                )
                .hasValueSatisfying(films ->
                        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("mpa", Mpa.builder()
                                        .id(3)
                                        .name("PG-13")
                                        .build())
                                .hasFieldOrPropertyWithValue("name", "film second call")
                                .hasFieldOrPropertyWithValue("description", "description two")
                                .hasFieldOrPropertyWithValue("duration", 500)
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 3,25))
                )
                .hasValueSatisfying(users ->
                        assertThat(users.size()).isEqualTo(3)
                );
    }

    @Test
    @Order(5)
    public void createLikeTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        userStorage.create(userTest);
    filmStorage.createLike(1,3);
    List<Film> popularFilms = filmStorage.getPopularFilms(1);
    assertEquals(popularFilms.get(0).getId(), 3);
    }

    @Test
    @Order(6)
    public void deleteLikeTest() {
        filmStorage.deleteLike(1, 3);
        filmStorage.createLike(1, 2);
        List<Film> popularFilms = filmStorage.getPopularFilms(1);
        assertEquals(popularFilms.get(0).getId(), 2);
    }

    @Test
    @Order(7)
    public void deleteAndCreateNonExistLikeTest() {
        NotFoundException filmExDel = Assertions.assertThrows(NotFoundException.class, () -> filmStorage.deleteLike(1,-5));
        assertEquals("Film not found", filmExDel.getMessage());
        NotFoundException userExDel = Assertions.assertThrows(NotFoundException.class, () -> filmStorage.deleteLike(-5,1));
        assertEquals("User not found", userExDel.getMessage());
        NotFoundException filmExCreate = Assertions.assertThrows(NotFoundException.class, () -> filmStorage.createLike(1,-5));
        assertEquals("Film not found", filmExCreate.getMessage());
        NotFoundException userExCreate  = Assertions.assertThrows(NotFoundException.class, () -> filmStorage.createLike(-5,1));
        assertEquals("User not found", userExCreate.getMessage());
    }

    @Test
    @Order(8)
    public void filmUpdateWithGenreTest() {
        Film filmTestUpdate = Film.builder()
                .name("update genre film")
                .description("update genre description")
                .releaseDate(LocalDate.of(1977, 3, 25))
                .duration(1000)
                .mpa(Mpa.builder()
                        .id(2)
                        .build())
                .genres(List.of(Genre.builder()
                        .id(3)
                        .build()))
                .id(1)
                .build();
        filmStorage.update(filmTestUpdate);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film ->
                        assertEquals(film.getGenres().get(0), Genre.builder()
                                                                    .id(3)
                                                                    .name("Мультфильм")
                                                                    .build())
                );
    }

    @Test
    @Order(9)
    public void testUpdateRemoveGenreFilmTest() {
        Film filmTestUpdate = Film.builder()
                .name("update delete genre film")
                .description("update delete genre description")
                .releaseDate(LocalDate.of(1977, 3, 25))
                .duration(1000)
                .genres(List.of())
                .mpa(Mpa.builder()
                        .id(2)
                        .build())
                .id(1)
                .build();
        filmStorage.update(filmTestUpdate);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film ->
                        assertTrue(film.getGenres().isEmpty())
                );
    }
}
