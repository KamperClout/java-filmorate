package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;


import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validate(user);
        log.info("Пользователь добавлен: " + user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        validateFoundUser(user.getId(), user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь обновлен: " + user);
        return userStorage.update(user);
    }

    public void delete(User user) {
        if (get(user.getId()) == null) {
            throw new UserNotFoundException("Пользователь с ID = " + user.getId() + " не найден");
        }
        userStorage.delete(user);
        log.info("Пользователь удален: " + user);
    }

    public List<User> findAll() {
        log.info("Список всех пользователей");
        return userStorage.findAll();
    }

    public User get(int id) {
        log.info("Пользователь с id = " + id + "получен");
        return userStorage.get(id);
    }

    public void addFriend(int idUser, int idFriend) {
        validateFoundUser(idUser, idFriend);
        friendshipStorage.addFriend(idUser, idFriend);
        log.info("Друг успешно добавлен");
    }

    public void removeFriend(int idUser, int idFriend) {
        validateFoundUser(idUser, idFriend);
        friendshipStorage.removeFriend(idUser, idFriend);
        log.info("Друг успешно удален");
    }

    public List<User> getMutualFriends(int idUser1, int idUser2) {
        validateFoundUser(idUser1, idUser2);
        List<User> list = friendshipStorage.getMutualFriends(idUser1, idUser2);
        log.info("Общие друзья пользователей с ID " + " {} and {} {} ", idUser1, idUser2, list);
        return list;
    }

    public List<User> getFriends(int id) {
        log.info("Список друзей пользователся с id = " + id);
        return friendshipStorage.getFriends(id);
    }

    private void validateFoundUser(Integer userId, Integer friendId) {
        userStorage.get(userId);
        userStorage.get(friendId);
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
