package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody @Validated({Marker.OnCreate.class}) User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        validateName(user.getName(), user);

        user.setId(getNextId());
        User createdUser = new User(user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        users.put(user.getId(), user);

        log.info("Успешно отработан HTTP-запрос на создание пользователя: {}", user);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody @Validated({Marker.OnUpdate.class}) User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);

        Long id = user.getId();
        if (id == null) {
            log.error("Не задан id пользователя");
            throw new ValidationException("Не задан id пользователя");
        }
        if (!users.containsKey(id)) {
            log.error("Пользователь с id = {} не найден", id);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        validateName(user.getName(), user);

        String email = user.getEmail();
        if (email == null) {
            email = users.get(id).getEmail();
        }

        User createdUser = new User(id, email, user.getLogin(), user.getName(), user.getBirthday());
        users.put(id, user);

        log.info("Успешно отработан HTTP-запрос на обновление пользователя: {}", user);
        return createdUser;
    }

    private void validateName(String name, User user) {
        if (name == null || name.isBlank()) {
            String login = user.getLogin();

            log.warn("Имя {} не валидно. В качестве имени будет использован логин {}", name, login);
            user.setName(login);
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
