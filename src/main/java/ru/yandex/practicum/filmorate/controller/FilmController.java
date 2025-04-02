package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на создание фильма: {}", film);
        validateReleaseDate(film.getReleaseDate());

        film.setId(getNextId());
        Film createdFilm = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration());
        films.put(film.getId(), film);

        log.info("Успешно отработан HTTP-запрос на создание фильма: {}", film);
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);

        Long id = film.getId();
        if (id == null) {
            log.error("Не задан id фильма");
            throw new ValidationException("Не задан id фильма");
        }
        if (!films.containsKey(id)) {
            log.error("Фильм с id = {} не найден", id);
            throw new FilmNotFoundException("Фильм не найден!");
        }
        validateReleaseDate(film.getReleaseDate());

        Film createdFilm = new Film(id, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        films.put(id, film);

        log.info("Успешно отработан HTTP-запрос на обновление фильма: {}", film);
        return createdFilm;
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(Film.EARLIEST_RELEASE_DATE)) {
            log.error("Дата релиза {} не валидна", releaseDate);

            throw new ValidationException(String.format("Дата релиза не может быть ранее %s.", Film.EARLIEST_RELEASE_DATE));
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
