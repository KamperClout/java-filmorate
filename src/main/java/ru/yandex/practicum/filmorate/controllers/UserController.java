package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("получен запрос GET/users");
        return userService.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("получен запрос POST/users с параметрами {}", user);
        return userService.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("получен запрос PUT/users с параметрами {}", user);
        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public User get(@PathVariable int id) {
        log.info("получен запрос GET/users/{id} с параметрами id={}", id);
        return userService.get(id);
    }

    @DeleteMapping("/users")
    public void delete(@RequestBody User user) {
        log.info("получен запрос DELETE/users с параметрами {}", user);
        userService.delete(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("получен запрос PUT/users/{id}/friends/{friendId} с параметрами id = {}, friendId = {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("получен запрос DELETE/users/{id}/friends/{friendId} с параметрами id = {}, friendId = {}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> userFriends(@PathVariable int id) {
        log.info("получен запрос GET/users/{id}/friends с параметрами id = {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("получен запрос GET/users/{id}/friends/common/{otherId} с параметрами id = {}, otherId = {}", id, otherId);
        return userService.getMutualFriends(id, otherId);
    }

}
