package ru.mirea.infinitejourneysbackend.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.mirea.infinitejourneysbackend.validation.validators.FilesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FilesValidator.class})
public @interface ValidFiles {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

