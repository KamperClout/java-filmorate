package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("получен запрос GET/films");
        return films.values();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        film.setId(++filmId);
        log.info("получен запрос POST/films с параметрами {}", film);
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("получен запрос PUT/films с параметрами {}", film);
        if (!films.containsKey(film.getId())) {
            log.error("фильм с id={} не найден", film.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "фильм с id= " + film.getId() + " не найден");
        }
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    private void validate(Film film) {
        if (film.getName().isBlank()) {
            log.warn("название фильма не может быть пустым");
            throw new ValidationException("название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("описание фильма не может быть больше 200 символов");
            throw new ValidationException("описание фильма не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <=0) {
            log.warn("продолжительность должна быть положительной");
            throw new ValidationException("продолжительность должна быть положительной");
        }
    }
}
