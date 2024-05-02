package ru.mirea.infinitejourneysbackend.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.infinitejourneysbackend.exception.country.CountryNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.country.CountryNotUniqueNameProblem;

@RestControllerAdvice
public class CountryExceptionControllerAdvice {

    @ExceptionHandler(CountryNotUniqueNameProblem.class)
    public ResponseEntity<String> handleUserNotUniqueUsername(CountryNotUniqueNameProblem ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Country with name " + ex.getMessage() + " already exists.");
    }

    @ExceptionHandler(CountryNotFoundProblem.class)
    public ResponseEntity<String> handleCountryNotFoundProblem(CountryNotFoundProblem ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body("Country not found with ID: " + ex.getMessage());
    }
}
