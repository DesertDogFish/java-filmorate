package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class OlderThanValidator implements ConstraintValidator<OlderThan, LocalDate> {

    protected LocalDate minDate;

    @Override
    public void initialize(OlderThan constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isAfter(minDate);
    }
}