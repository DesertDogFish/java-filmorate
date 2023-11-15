package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OlderThanValidator.class)
@Documented
public @interface OlderThan {
    String message() default "${validatedValue} должна быть позже параметра";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}
