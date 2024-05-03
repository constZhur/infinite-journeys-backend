package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateTourPriceRequest(
        @NotNull(message = "Цена не может быть пустой")
        @Positive(message = "Цена должна быть положительной")
        Double price
) { }
