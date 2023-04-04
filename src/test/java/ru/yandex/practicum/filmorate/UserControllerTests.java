package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTests {

    @Test
    void defaultCreateUserTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        UserController userController = new UserController();
        userController.create(userTest);
        assertEquals(userController.findAll().get(0), userTest);
    }

    @Test
    void createUserWithEmptyNameTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        UserController userController = new UserController();
        userController.create(userTest);
        userTest.setName(userTest.getLogin());
        assertEquals(userController.findAll().get(0), userTest);
    }

    @Test
    void defaultUpdateUserTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        User userTestUpdate = User.builder()
                .login("doloreUpdate")
                .name("Nick Name Update")
                .email("mailupdate@mail.ru")
                .birthday(LocalDate.of(1976, 9, 10))
                .id(1)
                .build();
        UserController userController = new UserController();
        userController.create(userTest);
        userController.update(userTestUpdate);
        assertEquals(userController.findAll().get(0), userTestUpdate);
    }

    @Test
    void updateUserWithEmptyNameTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        User userTestUpdate = User.builder()
                .login("doloreUpdate")
                .name("")
                .email("mailupdate@mail.ru")
                .birthday(LocalDate.of(1976, 9, 10))
                .id(1)
                .build();
        UserController userController = new UserController();
        userController.create(userTest);
        userController.update(userTestUpdate);
        userTestUpdate.setName(userTestUpdate.getLogin());
        assertEquals(userController.findAll().get(0), userTestUpdate);
    }
}