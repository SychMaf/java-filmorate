package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MinRealiseValidator implements ConstraintValidator<StartFilmTime, LocalDate> {
    @Override
    public void initialize(StartFilmTime past) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext ctx) {
        return (localDate != null && localDate.isAfter(LocalDate.of(1895, 12, 28)));
    }
}