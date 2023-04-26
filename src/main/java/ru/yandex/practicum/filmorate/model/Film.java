package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import ru.yandex.practicum.filmorate.validator.StartFilmTime;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NonNull
public class Film {
    @JsonIgnore
    @Builder.Default
    private Set<Integer> likes = new HashSet<>();
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @StartFilmTime
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    public void addLike(Integer userId) {
        likes.add(userId);
    }

    public void deleteLike(Integer userId) {
        likes.remove(userId);
    }
}