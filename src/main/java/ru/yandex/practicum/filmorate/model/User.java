package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class User {
    @JsonIgnore
    @Builder.Default
    private Set<Integer> friends = new HashSet<>();
    private int id;
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public void addFriend(Integer friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Integer friendId) {
        friends.remove(friendId);
    }
}