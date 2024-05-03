package ru.mirea.infinitejourneysbackend.domain.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на покупку заказа")
public record BuyOrderRequest(
        @Schema(description = "Идентификатор тура", example = "1")
        @NotNull(message = "Идентификатор тура не может быть пустым")
        Long tourId
) { }
