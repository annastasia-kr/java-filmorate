package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.deserializer.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    public static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    public static final int MAX_LENGTH = 200;

    private Long id;

    @NotEmpty(message = "Название фильма должно быть заполнено")
    private String name;

    @Size(message = "Превышена максимальная длина описания", max = MAX_LENGTH)
    private String description;

    private LocalDate releaseDate;

    @DurationMin(message = "Длительность не валидна", minutes = 1)
    @JsonFormat(pattern = "MINUTES")
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

}
