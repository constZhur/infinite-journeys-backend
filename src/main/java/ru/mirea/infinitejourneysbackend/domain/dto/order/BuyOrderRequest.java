package ru.mirea.infinitejourneysbackend.domain.dto.order;

import jakarta.validation.constraints.NotNull;

public record BuyOrderRequest(
        @NotNull(message = "Идентификатор тура не может быть пустым")
        Long tourId
) { }
