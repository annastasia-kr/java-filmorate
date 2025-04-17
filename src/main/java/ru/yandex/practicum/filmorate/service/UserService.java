package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        Optional<User> optionalUser = Optional.ofNullable(userStorage.findById(userId));
        Optional<User> optionalFriend = Optional.ofNullable(userStorage.findById(friendId));
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        if (optionalFriend.isEmpty()) {
            log.error("Пользователь с id = {} не найден", friendId);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        User toUpdateUser = optionalUser.get();
        toUpdateUser.addFriend(friendId);
        userStorage.updateUser(toUpdateUser);

        User toUpdateFriend = optionalFriend.get();
        toUpdateFriend.addFriend(userId);
        userStorage.updateUser(toUpdateFriend);
    }

    public void removeFriend(Long userId, Long friendId) {
        Optional<User> optionalUser = Optional.ofNullable(userStorage.findById(userId));
        Optional<User> optionalFriend = Optional.ofNullable(userStorage.findById(friendId));
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        if (optionalFriend.isEmpty()) {
            log.error("Пользователь с id = {} не найден", friendId);
            throw new UserNotFoundException("Пользователь не найден!");
        }

        User toUpdateUser = optionalUser.get();
        toUpdateUser.removeFriend(friendId);
        userStorage.updateUser(toUpdateUser);

        User toUpdateFriend = optionalFriend.get();
        toUpdateFriend.removeFriend(userId);
        userStorage.updateUser(toUpdateFriend);

    }

    public Collection<?> getCommonFriends(Long userId, Long otherId) {
        Optional<User> optionalUser = Optional.ofNullable(userStorage.findById(userId));
        Optional<User> optionalOtherUser = Optional.ofNullable(userStorage.findById(otherId));
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        if (optionalOtherUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", otherId);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        Set<Long> friends = optionalUser.get().getFriends();
        friends.retainAll(optionalOtherUser.get().getFriends());

        return friends.stream()
                .map(userStorage::findById)
                .toList();
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User create(User user) {
        validateName(user.getName(), user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        Long id = user.getId();
        if (id == null) {
            log.error("Не задан id пользователя");
            throw new ValidationException("Не задан id пользователя");
        }

        validateName(user.getName(), user);
        return userStorage.updateUser(user);
    }

    public Collection<User> getFriends(long id) {
        Optional<User> optionalUser = Optional.ofNullable(userStorage.findById(id));
        if (optionalUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", id);
            throw new UserNotFoundException("Пользователь не найден!");
        }
        return optionalUser.get().getFriends().stream()
                .map(userStorage::findById)
                .toList();

    }

    private void validateName(String name, User user) {
        if (name == null || name.isBlank()) {
            String login = user.getLogin();

            log.warn("Имя {} не валидно. В качестве имени будет использован логин {}", name, login);
            user.setName(login);
        }
    }

}
