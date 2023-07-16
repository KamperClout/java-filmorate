package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void delete(Film film) {
        filmStorage.delete(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film get(int id) {
        return filmStorage.get(id);
    }

    public void addLike(int filmId, int userId) {
        Film film = get(filmId);
        User user = userStorage.get(userId);

        film.getLikes().add(user.getId());
    }

    public void removeLike(int filmId, int userId) {
        Film film = get(filmId);
        User user = userStorage.get(userId);

        film.getLikes().remove(user.getId());
    }

    public List<Film> getTopFilms(int count) {
        List<Film> films = findAll();
        return films.stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size(), Comparator.reverseOrder()))
                .limit(count).collect(Collectors.toList());
    }

}
