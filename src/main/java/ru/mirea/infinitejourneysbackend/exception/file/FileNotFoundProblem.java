package ru.mirea.infinitejourneysbackend.exception.file;

import lombok.Getter;

@Getter
public class FileNotFoundProblem extends RuntimeException {
    private final String message;

    public FileNotFoundProblem(String message) {
        this.message = message;
    }
}
