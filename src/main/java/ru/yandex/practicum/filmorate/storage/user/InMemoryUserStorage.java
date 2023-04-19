package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int globalId = 0;

    public List<User> findAll() {
        log.info("Return user list");
        return new ArrayList<>(users.values());
    }

    public User findById(Integer id) {
        if (!users.containsKey(id)) {
            log.error("User not found");
            throw new NotFoundException("User not found");
        }
        return users.get(id);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.error("User name empty");
            user.setName(user.getLogin());
        }
        globalId++;
        user.setId(globalId);
        users.put(user.getId(), user);
        log.info("User successfully created");
        return user;
    }

    public User update(User user) {
        int replaceId = user.getId();
        if (!users.containsKey(replaceId)) {
            log.error("User not found");
            throw new NotFoundException("User not found");
        }
        if (user.getName().isEmpty() || user.getName().isBlank() || user.getName() == null) {
            log.error("User name empty");
            user.setName(user.getLogin());
        }
        users.put(replaceId, user);
        log.info("User successfully updated");
        return user;
    }
}