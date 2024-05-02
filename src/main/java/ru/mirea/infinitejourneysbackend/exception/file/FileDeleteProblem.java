package ru.mirea.infinitejourneysbackend.exception.file;

import lombok.Getter;

@Getter
public class FileDeleteProblem extends RuntimeException {
    private final String message;

    public FileDeleteProblem(String message) {
        this.message = message;
    }
}
