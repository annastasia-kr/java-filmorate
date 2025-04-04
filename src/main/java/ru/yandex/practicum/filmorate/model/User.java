package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.marker.Marker;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @Email(message = "Электронная почта не валидна", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Size(min = 1, groups = {Marker.OnUpdate.class}, message = "Электронная почта не может быть пустой")
    @NotEmpty(groups = {Marker.OnCreate.class}, message = "Электронная почта должна быть заполнена")
    private String email;

    @NotEmpty(message = "Логин должен быть заполнен", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Pattern(message = "Логин не может содержать пробелов", regexp = "\\b\\pL\\w*(?:-\\pL\\w*)*\\b",
            groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private LocalDate birthday;

}
