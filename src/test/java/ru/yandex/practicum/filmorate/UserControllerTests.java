package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

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
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.create(userTest);
        assertEquals(inMemoryUserStorage.findAll().get(0), userTest);
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
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.create(userTest);
        userTest.setName(userTest.getLogin());
        assertEquals(inMemoryUserStorage.findAll().get(0), userTest);
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
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.create(userTest);
        inMemoryUserStorage.update(userTestUpdate);
        assertEquals(inMemoryUserStorage.findAll().get(0), userTestUpdate);
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
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.create(userTest);
        inMemoryUserStorage.update(userTestUpdate);
        userTestUpdate.setName(userTestUpdate.getLogin());
        assertEquals(inMemoryUserStorage.findAll().get(0), userTestUpdate);
    }

    @Test
    void addAndDeleteFriendTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();
        User anotherUserTest = User.builder()
                .login("anotherLogin")
                .name("")
                .email("anoherMail@mail.ru")
                .birthday(LocalDate.of(1977, 9, 10))
                .id(2)
                .build();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryUserStorage.create(userTest);
        inMemoryUserStorage.create(anotherUserTest);
        UserService userService = new UserService(inMemoryUserStorage);
        userService.addFriend(1, 2);
        assertEquals(userService.getFriends(1).size(), 1);
        assertEquals(userService.getLinkedFriends(1, 2).size(), 0);
        userService.removeFriend(2, 1);
        assertEquals(userService.getFriends(2).size(), 0);
    }
}