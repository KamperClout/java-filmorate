package controller;


import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private final FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
            new InMemoryUserStorage()));


    @Test
    void create() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        Film created = filmController.create(film);
        assertTrue(created.getId() > 0);
        assertEquals("description", created.getDescription());
        assertEquals(200, created.getDuration());
        assertEquals(LocalDate.of(2015, 4, 15), created.getReleaseDate());
    }

    @Test
    void createWithEmptyName() {
        Film film = Film.builder()
                .name("")
                .description("description")
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createWithDescription200() {
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(200))
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        Film created = filmController.create(film);
        assertNotNull(created);
    }

    @Test
    void createWithDescriptionOver200() {
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(201))
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createWithDurationLessThan0() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(-100)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createWithDuration0() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(0)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void createWithEarlyReleaseDate() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(200)
                .releaseDate(LocalDate.of(1800, 4, 15))
                .build();
        assertThrows(ValidationException.class,
                () -> filmController.create(film));
    }

    @Test
    void update() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        filmController.create(film);
        Film updateFilm = film.toBuilder().id(film.getId()).name("hehe17").duration(150).build();
        Film updated = filmController.update(updateFilm);
        assertEquals(1, filmController.findAll().size());
        assertEquals("hehe17", updated.getName());
        assertEquals(150, updated.getDuration());
        assertEquals(LocalDate.of(2015, 4, 15), updated.getReleaseDate());
        assertEquals("description", updated.getDescription());

    }

    @Test
    void updateNotFoundFilm() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        filmController.create(film);
        Film updateFilm = film.toBuilder().id(2).name("hehe17").duration(150).build();
        assertThrows(FilmNotFoundException.class,
                () -> filmController.update(updateFilm));
    }

    @Test
    void shouldGetAll() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(200)
                .releaseDate(LocalDate.of(2015, 4, 15))
                .build();
        Film film2 = film.toBuilder().id(2).name("hehe17").duration(150).build();
        filmController.create(film);
        filmController.create(film2);
        assertEquals(2, filmController.findAll().size());
    }

}
