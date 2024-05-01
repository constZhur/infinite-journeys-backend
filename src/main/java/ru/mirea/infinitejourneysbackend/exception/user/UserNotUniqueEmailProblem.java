package ru.mirea.infinitejourneysbackend.exception.user;

import lombok.Getter;

@Getter
public class UserNotUniqueEmailProblem extends RuntimeException {

    private final String message;

    public UserNotUniqueEmailProblem(String message) {
        this.message = message;
    }
}
