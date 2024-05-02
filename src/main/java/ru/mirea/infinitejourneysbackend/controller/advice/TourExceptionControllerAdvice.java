package ru.mirea.infinitejourneysbackend.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.infinitejourneysbackend.exception.tour.TourNotFoundProblem;

@RestControllerAdvice
public class TourExceptionControllerAdvice {

    @ExceptionHandler(TourNotFoundProblem.class)
    public ResponseEntity<String> handleTourNotFoundProblem(TourNotFoundProblem ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body("Tour not found with ID: " + ex.getMessage());
    }
}
