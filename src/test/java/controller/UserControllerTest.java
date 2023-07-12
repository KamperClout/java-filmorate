package controller;


import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserControllerTest {
    private final UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void create() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        User createdUser = userController.create(user);
        assertTrue(createdUser.getId() > 0);
        assertEquals("kamper.inc@yandex.ru", createdUser.getEmail());
        assertEquals("kain", createdUser.getLogin());
        assertEquals("Pavel", createdUser.getName());
        assertEquals(LocalDate.of(2003, 10, 17), createdUser.getBirthday());
    }

    @Test
    void createWithEmptyEmail() {
        User user = User.builder()
                .email("")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

    @Test
    void createWithNotValidEmail() {
        User user = User.builder()
                .email("yandex.ru")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

    @Test
    void createWithEmptyLogin() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

    @Test
    void createWithSpacesLogin() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("ka in")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

    @Test
    void createWithEmptyName() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("kain")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        User createdUser = userController.create(user);
        assertEquals(createdUser.getName(), user.getLogin());
    }

    @Test
    void createWithFutureDateBorn() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.now().plusYears(1))
                .build();
        assertThrows(ValidationException.class,
                () -> userController.create(user));
    }

    @Test
    void update() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        userController.create(user);
        User userUpdate = user.toBuilder().id(user.getId()).email("pavel13@mail.ru").login("so4l").build();
        userController.update(userUpdate);
        assertEquals(1, userController.findAll().size());
        assertEquals("Pavel", userUpdate.getName());
        assertEquals("pavel13@mail.ru", userUpdate.getEmail());
        assertEquals("so4l", userUpdate.getLogin());
        assertEquals(LocalDate.of(2003, 10, 17), userUpdate.getBirthday());
    }

    @Test
    void updateWithNoFoundUser() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        userController.create(user);
        User userUpdate = user.toBuilder().id(2).email("pavel13@mail.ru").login("so4l").build();
        assertThrows(UserNotFoundException.class,
                () -> userController.update(userUpdate));
    }

    @Test
    void shouldGetAllUsers() {
        User user = User.builder()
                .email("kamper.inc@yandex.ru")
                .login("kain")
                .name("Pavel")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        userController.create(user);
        User user2 = user.toBuilder().id(2).email("pavel13@mail.ru").login("so4l").build();
        userController.create(user2);
        assertEquals(2, userController.findAll().size());
    }
}
