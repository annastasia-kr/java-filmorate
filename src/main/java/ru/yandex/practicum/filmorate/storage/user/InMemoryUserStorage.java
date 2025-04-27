package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmailIsTakenException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersEmail = new HashSet<>();

    @Override
    public Collection<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (usersEmail.contains(user.getEmail())) {
            throw new EmailIsTakenException("Данный email уже используется!");
        }
        user.setId(getNextId());
        User createdUser = new User(user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getFriends());
        usersEmail.add(user.getEmail());
        users.put(user.getId(), user);
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден!");
        }
        String email = user.getEmail();
        String lastEmail = users.get(user.getId()).getEmail();
        if (email == null) {
            email = lastEmail;
        } else if (!email.equals(lastEmail)) {
            if (usersEmail.contains(email)) {
                throw new EmailIsTakenException("Данный email уже используется!");
            }
            usersEmail.add(email);
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
