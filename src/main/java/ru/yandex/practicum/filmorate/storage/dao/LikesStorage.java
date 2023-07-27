package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopular(Integer count);
}
