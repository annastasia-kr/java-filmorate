package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PutMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Получен HTTP-запрос на добавление лайка фильму с id = {}", id);
        filmService.addLike(id, userId);
        log.info("Успешно отработан HTTP-запрос на добавление лайка");
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Получен HTTP-запрос на удаление лайка фильма с id = {}", id);
        filmService.removeLike(id, userId);
        log.info("Успешно отработан HTTP-запрос на удаление лайка");
    }

    @GetMapping("/films/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<?> mostPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос на вывод первых {} наиболее популярных фильмов", count);
        return filmService.mostPopular(count);
    }

    @GetMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilms() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return filmService.getFilms();
    }

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на создание фильма: {}", film);

        Film createdFilm = filmService.createFilm(film);

        log.info("Успешно отработан HTTP-запрос на создание фильма: {}", film);
        return createdFilm;
    }

    @PutMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public Film update(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);

        Film updatedFilm = filmService.update(film);

        log.info("Успешно отработан HTTP-запрос на обновление фильма: {}", film);
        return updatedFilm;
    }
}
