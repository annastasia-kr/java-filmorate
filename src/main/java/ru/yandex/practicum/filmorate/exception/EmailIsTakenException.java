package ru.yandex.practicum.filmorate.exception;

public class EmailIsTakenException extends RuntimeException {

    public EmailIsTakenException(String message) {
        super(message);
    }
}
