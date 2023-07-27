package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    void getAllFilms() {
        Film film1 = Film.builder()
                .id(1)
                .name("Walking")
                .description("The Walking around us")
                .releaseDate(LocalDate.now())
                .duration(200)
                .genres(new HashSet<>()).mpa(mpaDbStorage.getRatingMpaById(1))
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Avengers")
                .description("IronMan Is The Best actor playing")
                .releaseDate(LocalDate.now())
                .duration(180)
                .genres(new HashSet<>()).mpa(mpaDbStorage.getRatingMpaById(1))
                .build();
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        Collection<Film> films = filmDbStorage.findAll();

        assertThat(films).hasSize(2);
    }

    @Test
    void createFilm() {
        Film film = Film.builder()
                .id(2)
                .name("Avengers")
                .description("IronMan Is The Best actor playing")
                .releaseDate(LocalDate.now())
                .duration(180)
                .genres(new HashSet<>()).mpa(mpaDbStorage.getRatingMpaById(2))
                .build();
        filmDbStorage.create(film);
        Film filmOptional = filmDbStorage.get(1);

        assertEquals(filmOptional.getId(), 1);
    }

    @Test
    void getFilmById() {
        Film film = Film.builder()
                .id(2)
                .name("Avengers")
                .description("IronMan Is The Best actor playing")
                .releaseDate(LocalDate.now())
                .duration(180)
                .genres(new HashSet<>()).mpa(mpaDbStorage.getRatingMpaById(1))
                .build();
        filmDbStorage.create(film);

        assertEquals(filmDbStorage.get(1).getId(), film.getId());
    }

    @Test
    public void getAllUsers() {
        User user1 = User.builder()
                .id(1)
                .name("Paul")
                .login("kamperClout")
                .email("kamper.inc@Yandex.ru")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        User user2 = User.builder()
                .id(2)
                .name("Maxi")
                .login("maxcool")
                .email("max.ty@mail.ru")
                .birthday(LocalDate.of(1999, 7, 19))
                .build();
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        List<User> users = userDbStorage.findAll();

        assertThat(users).contains(user1);
        assertThat(users).contains(user2);
    }

    @Test
    public void createUser() {
        User user = User.builder()
                .id(1)
                .name("Paul")
                .login("kamperClout")
                .email("kamper.inc@Yandex.ru")
                .birthday(LocalDate.of(2003, 10, 17))
                .build();
        userDbStorage.create(user);
        User userOptional = userDbStorage.get(1);

        assertEquals(userOptional.getId(), 1);
    }
}