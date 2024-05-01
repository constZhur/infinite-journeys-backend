package ru.mirea.infinitejourneysbackend.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.infinitejourneysbackend.exception.user.*;

@RestControllerAdvice
public class UserExceptionControllerAdvice {

    @ExceptionHandler(InvalidUserDataProblem.class)
    public ResponseEntity<String> handleInvalidUserDataProblem() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid user data provided.");
    }

    @ExceptionHandler(UserNotUniqueUsernameProblem.class)
    public ResponseEntity<String> handleUserNotUniqueUsername(UserNotUniqueUsernameProblem ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("User with username " + ex.getMessage() + " already exists.");
    }

    @ExceptionHandler(UserNotUniqueEmailProblem.class)
    public ResponseEntity<String> handleUserNotUniqueEmail(UserNotUniqueEmailProblem ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("User with email " + ex.getMessage() + " already exists.");
    }

    @ExceptionHandler(UserNotFoundProblem.class)
    public ResponseEntity<String> handleUserNotFoundProblem() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User not found.");
    }

    @ExceptionHandler(UserDeletedProblem.class)
    public ResponseEntity<String> handleUserDeletedProblem() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("User is deleted.");
    }

    @ExceptionHandler(ForbiddenAccessProblem.class)
    public ResponseEntity<String> handleForbiddenAccessProblem() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access is forbidden.");
    }

    @ExceptionHandler(InvalidUserPasswordProblem.class)
    public ResponseEntity<String> handleInvalidUserPasswordProblem() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid user password provided.");
    }
}
