package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    User update(User user);

    User findById(Integer userId);

    boolean addFriend(int firstUserId, int secondUserId);

    boolean deleteFriend(int firstUserId, int secondUserId);

    List<Integer> getFriendList(int userId);

}