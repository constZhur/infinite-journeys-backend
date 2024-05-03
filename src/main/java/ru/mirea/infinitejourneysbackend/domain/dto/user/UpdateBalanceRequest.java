package ru.mirea.infinitejourneysbackend.domain.dto.user;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateBalanceRequest(
        @NotNull(message = "Сумма не может быть пустой")
        @Positive(message = "Сумма должна быть положительной")
        Double amount
) { }
