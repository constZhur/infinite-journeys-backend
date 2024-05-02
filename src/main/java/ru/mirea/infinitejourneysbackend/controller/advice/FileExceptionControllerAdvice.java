package ru.mirea.infinitejourneysbackend.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.infinitejourneysbackend.exception.file.FileDeleteProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileDownloadProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileSaveProblem;

@RestControllerAdvice
public class FileExceptionControllerAdvice {

    @ExceptionHandler(FileSaveProblem.class)
    public ResponseEntity<String> handleFileSaveProblem(FileSaveProblem ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body("Error saving file with name: " + ex.getMessage());
    }

    @ExceptionHandler(FileNotFoundProblem.class)
    public ResponseEntity<String> handleFileNotFoundProblem(FileNotFoundProblem ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body("File not found with ID: " + ex.getMessage());
    }

    @ExceptionHandler(FileDownloadProblem.class)
    public ResponseEntity<String> handleFileDownloadProblem(FileDownloadProblem ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body("Error downloading file with ID: " + ex.getMessage());
    }

    @ExceptionHandler(FileDeleteProblem.class)
    public ResponseEntity<String> handleFileDeleteProblem(FileDeleteProblem ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body("Error deleting file with ID: " + ex.getMessage());
    }
}
