package ru.mirea.infinitejourneysbackend.exception.file;

import lombok.Getter;

@Getter
public class FileSaveProblem extends RuntimeException {
    private final String message;

    public FileSaveProblem(String message) {
        this.message = message;
    }
}
