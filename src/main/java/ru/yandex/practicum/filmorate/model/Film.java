package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import ru.yandex.practicum.filmorate.validator.StartFilmTime;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NonNull
public class Film {
    private Set<Integer> like;
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @StartFilmTime
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    public Set<Integer> getLike() {
        if (like == null) {
            like = new HashSet<>();
        }
        return like;
    }
}