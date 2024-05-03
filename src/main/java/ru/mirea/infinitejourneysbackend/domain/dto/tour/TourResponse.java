package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.file.UploadedFileResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Ответ на запрос информации о туре")
public record TourResponse(
        @Schema(description = "Идентификатор тура", example = "1")
        Long id,

        @Schema(description = "Название тура", example = "Тур")
        String title,

        @Schema(description = "Описание тура", example = "Описание тура")
        String description,

        @Schema(description = "Дата начала тура", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime startDate,

        @Schema(description = "Дата окончания тура", example = "2023-05-15T10:10:10.000+00:00")
        OffsetDateTime endDate,

        @Schema(description = "Цена тура", example = "1000.0")
        Double price,

        @Schema(description = "Страна")
        CountryResponse country,

        @Schema(description = "Продавец")
        UserResponse seller,

        @Schema(description = "Список вложений")
        List<UploadedFileResponse> attachments,

        @Schema(description = "Дата создания записи", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime createdAt,

        @Schema(description = "Дата последнего обновления записи", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime updatedAt
) { }
