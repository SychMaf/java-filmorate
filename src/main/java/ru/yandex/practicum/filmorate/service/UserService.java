package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
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
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        userStorage.update(user);
        userStorage.update(friend);
        log.info("friends added");
        return userStorage.findById(userId);
    }

    public User removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        userStorage.update(user);
        userStorage.update(friend);
        log.info("friends removed");
        return userStorage.findById(userId);
    }

    public List<User> getFriends(Integer userId) {
        User user = userStorage.findById(userId);
        Set<Integer> friendsId = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(userStorage.findById(id));
        }
        log.info("return friends list");
        return friends;
    }

    public List<User> getLinkedFriends(Integer userId, Integer otherId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(otherId);
        Set<Integer> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(friend.getFriends());
        List<User> linkedFriends = new ArrayList<>();
        for (Integer id : intersection) {
            linkedFriends.add(userStorage.findById(id));
        }
        log.info("return linked friends list");
        return linkedFriends;
    }
}