package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public GenreService(@Qualifier("GenreDbStorage") GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public void deleteAllGenresById(int filmId) {
        genreDbStorage.deleteAllGenresById(filmId);
    }

    public Genre getGenreById(int genreId) {
        Genre genre = genreDbStorage.getGenreById(genreId);
        if (genre == null) {
            throw new NotFoundException("Жанр не найден");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }
}
