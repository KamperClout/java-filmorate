package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public Film create(Film film) {
        validate(film);
        film.setId(++filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("фильм с id={} не найден", film.getId());
            throw new FilmNotFoundException("фильм с id= " + film.getId() + " не найден");
        }
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(int id) {
        if (!films.containsKey(id)) {
            log.error("фильм с id={} не найден", id);
            throw new FilmNotFoundException("фильм с id= " + id + " не найден");
        }
        return films.get(id);
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
        if (film.getDuration() <= 0) {
            log.warn("продолжительность должна быть положительной");
            throw new ValidationException("продолжительность должна быть положительной");
        }
    }
}
