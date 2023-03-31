package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int globalId = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<User> findAll() {
        log.info("Return user list");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (emails.contains(user.getEmail())) {
            log.error("User already exist");
            throw new ValidationException("User already exist");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.error("User name empty");
            user.setName(user.getLogin());
        }
        globalId++;
        user.setId(globalId);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
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
            user.setName(user.getEmail());
        }
        emails.remove(users.get(replaceId));
        users.put(replaceId, user);
        emails.add(user.getEmail());
        log.info("User successfully updated");
        return user;
    }
}