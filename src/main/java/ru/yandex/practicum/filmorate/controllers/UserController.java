package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;


    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("получен запрос GET/users");
        return users.values();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("получен запрос POST/users с параметрами {}", user);
        validate(user);
        user.setId(++userId);
        if (Optional.ofNullable(user.getName()).isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("получен запрос PUT/users с параметрами {}", user);
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id ={} не найден", user.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id = " + user.getId()
                    + " не найден");
        }
        validate(user);
        if (Optional.ofNullable(user.getName()).isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    private void validate(User user) {
        if (user.getEmail().isBlank()) {
            log.warn("email не может быть пустым");
            throw new ValidationException("email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("email должен содержать символ @");
            throw new ValidationException("email должен содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("логин не может содержать пробелов или быть пустым");
            throw new ValidationException("логин не может содержать пробелов или быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
