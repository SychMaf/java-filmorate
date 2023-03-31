package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.StartFilmTime;

import java.time.LocalDate;

@Data
@Builder
@NonNull
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @StartFilmTime
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
}