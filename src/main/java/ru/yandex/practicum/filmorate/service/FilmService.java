package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);
        log.info("Фильм обновлен: " + film);
        return filmStorage.update(film);
    }

    public void delete(Film film) {
        if (get(film.getId()) == null) {
            throw new FilmNotFoundException("Фильм с ID = " + film.getId() + " не найден");
        }
        filmStorage.delete(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film get(int id) {
        return filmStorage.get(id);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        if (film != null) {
            if (userStorage.get(userId) != null) {
                filmStorage.addLike(filmId, userId);
                log.info("Лайк добавлен");
            } else {
                throw new UserNotFoundException("Пользователь с ID = " + userId + " не найден");
            }
        } else {
            throw new FilmNotFoundException("Фильм с ID = " + filmId + " не найден");
        }
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        if (film != null) {
            if (userStorage.get(userId) != null) {
                filmStorage.removeLike(filmId, userId);
                log.info("Лайк удален");
            } else {
                throw new UserNotFoundException("Пользователь с ID = " + userId + " не найден");
            }
        } else {
            throw new FilmNotFoundException("Фильм с ID = " + filmId + " не найден");
        }
    }

    public List<Film> getTopFilms(int count) {
        log.info("Список популярных фильмов");
        return filmStorage.getPopular(count);
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
