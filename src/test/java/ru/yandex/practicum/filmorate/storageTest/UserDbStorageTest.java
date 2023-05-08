package ru.yandex.practicum.filmorate.storageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
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
public class UserDbStorageTest {
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Test
    @Order(1)
    public void createUserTest() {
        User userTest = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        Optional<User> userOptional = Optional.ofNullable(userStorage.create(userTest));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("email", "mail@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "dolore")
                                .hasFieldOrPropertyWithValue("name", "Nick Name")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1946, 8, 20))
                );
    }

    @Test
    @Order(2)
    public void userUpdateTest() {
        User userTestUpdate = User.builder()
                .login("doloreUpdate")
                .name("Nick Name Update")
                .email("mailupdate@mail.ru")
                .birthday(LocalDate.of(1976, 9, 10))
                .id(1)
                .build();
        Optional<User> userOptional = Optional.ofNullable(userStorage.update(userTestUpdate));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("email", "mailupdate@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "doloreUpdate")
                                .hasFieldOrPropertyWithValue("name", "Nick Name Update")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1976, 9, 10))
                );
    }

    @Test
    @Order(3)
    public void nonExistUserUpdateTest() {
        User userTestUpdate = User.builder()
                .login("doloreUpdate")
                .name("Nick Name Update")
                .email("mailupdate@mail.ru")
                .birthday(LocalDate.of(1976, 9, 10))
                .id(9999)
                .build();
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> userStorage.update(userTestUpdate));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    @Order(4)
    public void getAllUsersTest() {
        User userTest = User.builder()
                .login("friend")
                .name("friendName")
                .email("friend@mail.ru")
                .birthday(LocalDate.of(1956, 8, 20))
                .build();
        User anotherUserTest = User.builder()
                .login("another friend")
                .name("another friendName")
                .email("anotherfriend@mail.ru")
                .birthday(LocalDate.of(1996, 8, 20))
                .build();
        userStorage.create(userTest);
        userStorage.create(anotherUserTest);
        Optional<List<User>> optionalUserList = Optional.ofNullable(userStorage.findAll());
        assertThat(optionalUserList)
                .isPresent()
                .hasValueSatisfying(users ->
                        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("email", "mailupdate@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "doloreUpdate")
                                .hasFieldOrPropertyWithValue("name", "Nick Name Update")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1976, 9, 10))
                )
                .hasValueSatisfying(users ->
                        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("email", "friend@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "friend")
                                .hasFieldOrPropertyWithValue("name", "friendName")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1956, 8, 20))
                )
                .hasValueSatisfying(users ->
                        assertThat(users.size()).isEqualTo(3)
                );
    }

    @Test
    @Order(5)
    public void getEmptyFriendListTest() {
        Optional<List<Integer>> optionalFriendList = Optional.ofNullable(userStorage.getFriendList(1));
        assertThat(optionalFriendList)
                .isPresent()
                .hasValueSatisfying(friends ->
                        assertTrue(friends.isEmpty())
                );
    }

    @Test
    @Order(6)
    public void addFriendTest() {
        assertTrue(userStorage.addFriend(1, 2));
        assertTrue(userStorage.addFriend(1, 3));
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> userStorage.addFriend(1, -1));
        assertEquals("User not found", ex.getMessage());
        assertEquals(userStorage.getFriendList(1).toString(), "[2, 3]");
        assertEquals(2, userStorage.getFriendList(1).size());
        assertTrue(userStorage.getFriendList(2).isEmpty());
    }

    @Test
    @Order(7)
    public void createDuplicateFriendTest() {
        assertTrue(userStorage.addFriend(2, 1));
        assertTrue(userStorage.addFriend(2, 1));
        assertEquals(1, userStorage.getFriendList(2).size());
    }

    @Test
    @Order(8)
    public void deleteFriendTest() {
        assertTrue(userStorage.addFriend(2, 1));
        assertTrue(userStorage.deleteFriend(1, 2));
        assertEquals(userStorage.getFriendList(1).toString(), "[3]");
        assertEquals(userStorage.getFriendList(2).toString(), "[1]");
    }
}
