package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int globalId = 0;

    @GetMapping
    public List<User> findAll() {
        log.info("Return user list");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
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

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int replaceId = user.getId();
        if (!users.containsKey(replaceId)) {
            log.error("User not found");
            throw new ValidationException("User not found");
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