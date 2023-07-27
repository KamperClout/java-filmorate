package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public MpaService(@Qualifier("MpaDbStorage") MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public MPA getRatingMpaById(int ratingId) {
        MPA mpa = mpaDbStorage.getRatingMpaById(ratingId);
        if (mpa == null) {
            throw new NotFoundException("Рейтинг не найден");
        }
        log.info("Получен рейтинг с id = " + ratingId);
        return mpa;
    }

    public List<MPA> getRatingsMpa() {
        log.info("Список всех рейтингов");
        return mpaDbStorage.getRatingsMpa();
    }

}
