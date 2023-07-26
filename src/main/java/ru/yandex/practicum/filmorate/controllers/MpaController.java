package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPA> getRatingsMpa() {
        log.info("все MPA");
        return mpaService.getRatingsMpa();
    }

    @GetMapping("/{id}")
    public MPA getRatingMpaById(@PathVariable Integer id) {
        log.info("получение MPA c id {}", id);
        return mpaService.getRatingMpaById(id);
    }
}
