package ru.mirea.infinitejourneysbackend.exception.user;

import lombok.Getter;

@Getter
public class UserNotUniqueUsernameProblem extends RuntimeException {

    private final String message;

    public UserNotUniqueUsernameProblem(String message) {
        this.message = message;
    }
}
