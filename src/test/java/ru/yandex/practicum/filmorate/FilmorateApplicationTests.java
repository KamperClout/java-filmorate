package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userDbStorage.get(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAllUsers() {

        Optional<List<User>> userOptional = Optional.ofNullable(userDbStorage.findAll());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user.size() == 2).isTrue();
                        }
                );
    }

//    @Test
//    public void testDeleteUserById() {
//        userService.deleteUserFromFriendsWhenUserDeleted(1);
//        userStorage.delete(1);
//        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.getAll());
//
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user -> {
//                            assertThat(user.size() == 3).isTrue();
//                        }
//                );
//    }

    @Test
    public void testGetFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.get(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("film_id", 1)
                );
    }

    @Test
    public void testGetAllFilms() {

        Optional<List<Film>> films = Optional.ofNullable(filmDbStorage.findAll());

        assertThat(films)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user.size() == 3).isTrue();
                        }
                );
    }
}