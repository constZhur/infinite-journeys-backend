package ru.mirea.infinitejourneysbackend.domain.dto.country;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Ответ с информацией о стране")
public record CountryResponse(
        @Schema(description = "Идентификатор страны", example = "1")
        Long id,

        @Schema(description = "Название страны", example = "Россия")
        String name,

        @Schema(description = "Дата и время создания записи", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime createdAt,

        @Schema(description = "Дата и время последнего обновления записи", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime updatedAt
) { }
