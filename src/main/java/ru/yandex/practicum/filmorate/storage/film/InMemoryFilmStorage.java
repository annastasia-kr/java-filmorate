package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        Film createdFilm = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getLikedUsers());
        films.put(film.getId(), film);
        return createdFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм не найден!");
        }
        Film updatedFilm = new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getLikedUsers());
        films.put(film.getId(), film);
        return updatedFilm;
    }

    public Film findById(Long id) {
        if (!films.containsKey(id)) {
            return null;
        }
        Film foundedFilm = films.get(id);
        return new Film(foundedFilm.getId(), foundedFilm.getName(), foundedFilm.getDescription(), foundedFilm.getReleaseDate(),
                foundedFilm.getDuration(), foundedFilm.getLikedUsers());
    }

    @Override
    public Collection<?> mostPopular(int count) {
        return getFilms().stream()
                .sorted((film1, film2) -> film2.getLikedUsers().size() - film1.getLikedUsers().size())
                .limit(count)
                .toList();
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
