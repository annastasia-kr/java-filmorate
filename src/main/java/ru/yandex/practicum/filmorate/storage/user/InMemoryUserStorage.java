package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        User createdUser = new User(user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getFriends());
        users.put(user.getId(), user);
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id = {} не найден", user.getId());
            throw new UserNotFoundException("Пользователь не найден!");
        }
        String email = user.getEmail();
        if (email == null) {
            email = users.get(user.getId()).getEmail();
        }
        User updatedUser = new User(user.getId(), email, user.getLogin(), user.getName(), user.getBirthday(),
                user.getFriends());
        users.put(user.getId(), user);
        return updatedUser;
    }

    @Override
    public User findById(Long id) {
        if (!users.containsKey(id)) {
            return null;
        }
        User foundedUser = users.get(id);
        return new User(foundedUser.getId(), foundedUser.getEmail(), foundedUser.getLogin(), foundedUser.getName(),
                foundedUser.getBirthday(), foundedUser.getFriends());
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
