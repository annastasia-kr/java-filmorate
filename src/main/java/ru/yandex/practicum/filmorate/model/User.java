package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @Email(message = "Электронная почта не валидна")
    private String email;

    @NotEmpty(message = "Логин должен быть заполнен")
    @Pattern(message = "Логин не может содержать пробелов", regexp = "\\b\\pL\\w*(?:-\\pL\\w*)*\\b")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

}
