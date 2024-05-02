package ru.mirea.infinitejourneysbackend.exception.country;

import lombok.Getter;

@Getter
public class CountryNotUniqueNameProblem extends RuntimeException {
    private final String message;

    public CountryNotUniqueNameProblem(String message) {
        this.message = message;
    }
}
