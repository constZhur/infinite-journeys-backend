package ru.mirea.infinitejourneysbackend.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.infinitejourneysbackend.exception.mail.SendMessagingProblem;

@RestControllerAdvice
public class EmailExceptionControllerAdvice {

    @ExceptionHandler(SendMessagingProblem.class)
    public ResponseEntity<String> handleSendMessagingProblem(SendMessagingProblem ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body("Error sending email to the recipient: " + ex.getMessage());
    }
}
