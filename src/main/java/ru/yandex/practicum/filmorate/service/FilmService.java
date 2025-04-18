package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long id, Long userId) {
        Optional<Film> optionalFilm = Optional.ofNullable(filmStorage.findById(id));
        Optional<User> optionalUser = Optional.ofNullable(userStorage.findById(userId));
        if (optionalFilm.isEmpty()) {
            log.error("Фильм с id = {} не найден", id);
            throw new FilmNotFoundException("Фильм не найден!");
        }
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден!");
        }

        Film toUpdateFilm = optionalFilm.get();
        toUpdateFilm.addLike(userId);
        filmStorage.updateFilm(toUpdateFilm);

    }

    public void removeLike(Long id, Long userId) {
        Optional<Film> optionalFilm = Optional.ofNullable(filmStorage.findById(id));
        Optional<User> optionalUser = Optional.ofNullable(userStorage.findById(userId));
        if (optionalFilm.isEmpty()) {
            log.error("Фильм с id = {} не найден", id);
            throw new FilmNotFoundException("Фильм не найден!");
        }
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден!");
        }

        Film toUpdateFilm = optionalFilm.get();
        toUpdateFilm.removeLike(userId);
        filmStorage.updateFilm(toUpdateFilm);

    }

    public Collection<?> mostPopular(int count) {
        return filmStorage.mostPopular(count);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        validateReleaseDate(film.getReleaseDate());
        return filmStorage.createFilm(film);
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(Film.EARLIEST_RELEASE_DATE)) {
            log.error("Дата релиза {} не валидна", releaseDate);

            throw new ValidationException(String.format("Дата релиза не может быть ранее %s.", Film.EARLIEST_RELEASE_DATE));
        }
    }

    public Film update(Film film) {
        Long id = film.getId();
        if (id == null) {
            log.error("Не задан id фильма");
            throw new ValidationException("Не задан id фильма");
        }

        validateReleaseDate(film.getReleaseDate());
        return filmStorage.updateFilm(film);
    }
}
