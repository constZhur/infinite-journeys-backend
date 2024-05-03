package ru.mirea.infinitejourneysbackend.domain.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Запрос на баланса пользователя")
public record UpdateBalanceRequest(
        @Schema(description = "Сумма, добавляемая на баланс", example = "1000.0")
        @NotNull(message = "Сумма не может быть пустой")
        @Positive(message = "Сумма должна быть положительной")
        Double amount
) { }
