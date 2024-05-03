package ru.mirea.infinitejourneysbackend.exception.mail;

import lombok.Getter;

@Getter
public class SendMessagingProblem extends RuntimeException {
    private final String message;

    public SendMessagingProblem(String message) {
        this.message = message;
    }
}
