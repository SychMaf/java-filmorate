package ru.yandex.practicum.filmorate.InMemoryStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserInMemoryStorageTest {

    @Qualifier("InMemoryUserStorage") private final UserStorage userStorage;

    @Test
    @Order(1)
    void defaultCreateUserTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        userStorage.create(userTest);
        assertEquals(userStorage.findAll().get(0), userTest);
    }

    @Test
    @Order(2)
    void createUserWithEmptyNameTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        userStorage.create(userTest);
        userTest.setName(userTest.getLogin());
        assertEquals(userStorage.findById(2), userTest);
    }

    @Test
    @Order(3)
    void defaultUpdateUserTest() {
        User userTestUpdate = User.builder()
                .login("doloreUpdate")
                .name("Nick Name Update")
                .email("mailupdate@mail.ru")
                .birthday(LocalDate.of(1976, 9, 10))
                .id(1)
                .build();
        userStorage.update(userTestUpdate);
        assertEquals(userStorage.findAll().get(0), userTestUpdate);
    }

    @Test
    void updateUserWithEmptyNameTest() {
        User userTestUpdate = User.builder()
                .login("doloreUpdate")
                .name("")
                .email("mailupdate@mail.ru")
                .birthday(LocalDate.of(1976, 9, 10))
                .id(1)
                .build();
        userStorage.update(userTestUpdate);
        userTestUpdate.setName(userTestUpdate.getLogin());
        assertEquals(userStorage.findAll().get(0), userTestUpdate);
    }
}