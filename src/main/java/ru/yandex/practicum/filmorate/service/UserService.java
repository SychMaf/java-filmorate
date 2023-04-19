package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = inMemoryUserStorage.findById(userId);
        User friend = inMemoryUserStorage.findById(friendId);
        Set<Integer> friendsUser = user.getFriends();
        Set<Integer> friends = friend.getFriends();
        friendsUser.add(friendId);
        friends.add(userId);
        user.setFriends(friendsUser);
        friend.setFriends(friends);
        inMemoryUserStorage.update(user);
        inMemoryUserStorage.update(friend);
        log.info("friends added");
        return inMemoryUserStorage.findById(userId);
    }

    public User removeFriend(Integer userId, Integer friendId) {
        User user = inMemoryUserStorage.findById(userId);
        User friend = inMemoryUserStorage.findById(friendId);
        Set<Integer> friendsUser = user.getFriends();
        Set<Integer> friends = friend.getFriends();
        friendsUser.remove(friendId);
        friends.remove(userId);
        user.setFriends(friendsUser);
        friend.setFriends(friends);
        inMemoryUserStorage.update(user);
        inMemoryUserStorage.update(friend);
        log.info("friends removed");
        return inMemoryUserStorage.findById(userId);
    }

    public List<User> getFriends(Integer userId) {
        User user = inMemoryUserStorage.findById(userId);
        Set<Integer> friendsId = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(inMemoryUserStorage.findById(id));
        }
        log.info("return friends list");
        return friends;
    }

    public List<User> getLinkedFriends(Integer userId, Integer otherId) {
        User user = inMemoryUserStorage.findById(userId);
        User friend = inMemoryUserStorage.findById(otherId);
        Set<Integer> friendsUser = user.getFriends();
        Set<Integer> friendsOther = friend.getFriends();
        Set<Integer> intersection = new HashSet<>(friendsUser);
        intersection.retainAll(friendsOther);
        List<User> LinkedFriends = new ArrayList<>();
        for (Integer id : intersection) {
            LinkedFriends.add(inMemoryUserStorage.findById(id));
        }
        log.info("return linked friends list");
        return LinkedFriends;
    }
}