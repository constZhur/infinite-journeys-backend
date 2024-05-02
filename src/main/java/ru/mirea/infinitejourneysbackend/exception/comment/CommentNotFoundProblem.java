package ru.mirea.infinitejourneysbackend.exception.comment;

import lombok.Getter;

@Getter
public class CommentNotFoundProblem extends RuntimeException {
    private final String message;

    public CommentNotFoundProblem(String message){
        this.message = message;
    }
}
