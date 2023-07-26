package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("получен запрос GET/films");
        return filmService.findAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("получен запрос POST/films с параметрами {}", film);
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("получен запрос PUT/films с параметрами {}", film);
        return filmService.update(film);
    }

    @DeleteMapping("/films")
    public void delete(@RequestBody Film film) {
        log.info("получен запрос DELETE/films с параметрами {}", film);
        filmService.delete(film);
    }

    @GetMapping("/films/{id}")
    public Film get(@PathVariable int id) {
        log.info("получен запрос GET/films/{id} с параметрами id={}", id);
        return filmService.get(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("получен запрос PUT/films/{id}/like/{userId} с параметрами id={},userId={}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info("получен запрос PUT/films/{id}/like/{userId} с параметрами id={},userId={}", filmId, userId);
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("получен запрос GET/films/popular?count={count} с параметрами count={}", count);
        return filmService.getTopFilms(count);
    }
}
