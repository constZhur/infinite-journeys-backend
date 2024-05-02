package ru.mirea.infinitejourneysbackend.exception.file;

import lombok.Getter;

@Getter
public class FileDownloadProblem extends RuntimeException {
    private final String message;

    public FileDownloadProblem(String message) {
        this.message = message;
    }
}
