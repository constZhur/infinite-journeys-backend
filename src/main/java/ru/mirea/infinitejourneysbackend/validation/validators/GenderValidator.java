package ru.mirea.infinitejourneysbackend.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.mirea.infinitejourneysbackend.domain.model.Gender;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidGender;

import static java.util.Objects.isNull;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (isNull(s)) {
            return true;
        }
        try {
            var gender = Gender.valueOf(s);
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;

    }
}
