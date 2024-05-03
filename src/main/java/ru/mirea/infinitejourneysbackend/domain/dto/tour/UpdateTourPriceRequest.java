package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Запрос на обновление цены тура")
public record UpdateTourPriceRequest(
        @Schema(description = "Новая цена тура", example = "1200.0")
        @NotNull(message = "Цена не может быть пустой")
        @Positive(message = "Цена должна быть положительной")
        Double price
) { }
