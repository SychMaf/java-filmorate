package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank() || user.getName() == null) {
            log.error("User name empty");
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank() || user.getName() == null) {
            log.error("User name empty");
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public User getUser(int id) {
        return userStorage.findById(id);
    }

    public boolean addFriend(Integer userId, Integer friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public boolean removeFriend(Integer userId, Integer friendId) {
        return userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        List<Integer> friendsId = userStorage.getFriendList(userId);
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(userStorage.findById(id));
        }
        log.info("return friends list");
        return friends;
    }

    public List<User> getLinkedFriends(Integer userId, Integer otherId) {
        Set<Integer> intersection = new HashSet<>(userStorage.getFriendList(userId));
        intersection.retainAll(userStorage.getFriendList(otherId));
        List<User> linkedFriends = new ArrayList<>();
        for (Integer id : intersection) {
            linkedFriends.add(userStorage.findById(id));
        }
        log.info("return linked friends list");
        return linkedFriends;
    }
}