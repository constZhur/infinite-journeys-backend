package ru.mirea.infinitejourneysbackend.exception.tour;

import lombok.Getter;

@Getter
public class TourNotFoundProblem extends RuntimeException {
    private final String message;

    public TourNotFoundProblem(String message) {
        this.message = message;
    }
}
