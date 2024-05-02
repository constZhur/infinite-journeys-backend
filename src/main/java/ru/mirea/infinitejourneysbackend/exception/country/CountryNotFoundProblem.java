package ru.mirea.infinitejourneysbackend.exception.country;

import lombok.Getter;

@Getter
public class CountryNotFoundProblem extends RuntimeException {
    private final String message;

    public CountryNotFoundProblem(String message) {
        this.message = message;
    }
}
