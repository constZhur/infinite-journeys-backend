package ru.mirea.infinitejourneysbackend.domain.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Ответ с информацией о заказе")
public record OrderResponse(
        @Schema(description = "Идентификатор заказа", example = "1")
        Long id,

        @Schema(description = "Название тура", example = "Новый тур")
        String tourTitle,

        @Schema(description = "Описание тура", example = "Этот тот самый долгожданный тур")
        String tourDescription,

        @Schema(description = "Дата начала тура", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime startDate,

        @Schema(description = "Дата окончания тура", example = "2023-05-15T10:10:10.000+00:00")
        OffsetDateTime endDate,

        @Schema(description = "Название страны", example = "Россия")
        String countryName,

        @Schema(description = "Цена тура", example = "1000.0")
        Double price,

        @Schema(description = "Имя продавца", example = "Wenos")
        String sellerName,

        @Schema(description = "Дата и время создания заказа", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime createdAt
) { }
