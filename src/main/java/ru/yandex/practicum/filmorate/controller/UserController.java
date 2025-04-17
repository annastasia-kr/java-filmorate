package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Получен HTTP-запрос на добавление в друзья пользователем с id = {}, пользователя с id = {}",
                id, friendId);
        userService.addFriend(id, friendId);
        log.info("Успешно отработан HTTP-запрос на добавление в друзья пользователей");
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Получен HTTP-запрос на удаление из друзей пользователем с id = {}, пользователя с id = {}",
                id, friendId);
        userService.removeFriend(id, friendId);
        log.info("Успешно отработан HTTP-запрос на удаление из друзей пользователей");
    }

    @GetMapping("/users/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable("id") long id) {
        log.info("Получен HTTP-запрос на получение списка друзей пользователя id = {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<?> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        log.info("Получен HTTP-запрос на получение списка общих друзей пользователей id = {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return userService.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Validated({Marker.OnCreate.class}) User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);

        User createdUser = userService.create(user);

        log.info("Успешно отработан HTTP-запрос на создание пользователя: {}", user);
        return createdUser;
    }

    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public User update(@RequestBody @Validated({Marker.OnUpdate.class}) User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);

        User updatedUser = userService.updateUser(user);

        log.info("Успешно отработан HTTP-запрос на обновление пользователя: {}", user);
        return updatedUser;
    }

}
