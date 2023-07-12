package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @Override
    public User create(User user) {
        validate(user);
        user.setId(++userId);
        if (Optional.ofNullable(user.getName()).isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id ={} не найден", user.getId());
            throw new UserNotFoundException("Пользователь с id = " + user.getId()
                    + " не найден");
        }
        validate(user);
        if (Optional.ofNullable(user.getName()).isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(int id) {
        if (!users.containsKey(id)) {
            log.error("Пользователь с id ={} не найден", id);
            throw new UserNotFoundException("Пользователь с id = " + id
                    + " не найден");
        }
        return users.get(id);
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
