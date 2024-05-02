package ru.mirea.infinitejourneysbackend.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.infinitejourneysbackend.exception.comment.CommentNotFoundProblem;

@RestControllerAdvice
public class CommentExceptionControllerAdvice {

    @ExceptionHandler(CommentNotFoundProblem.class)
    public ResponseEntity<String> handleCommentNotFoundProblem(CommentNotFoundProblem ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body("Comment not found with ID: " + ex.getMessage());
    }
}
