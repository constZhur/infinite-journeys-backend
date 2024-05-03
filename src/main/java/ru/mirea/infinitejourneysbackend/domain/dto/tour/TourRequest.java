package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidFiles;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Запрос информации о туре")
public record TourRequest(
        @Schema(description = "Название тура", example = "Новый тур")
        String title,

        @Schema(description = "Описание тура", example = "Описание нового тура")
        String description,

        @Schema(description = "Дата начала тура", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime startDate,

        @Schema(description = "Дата окончания тура", example = "2023-05-15T10:10:10.000+00:00")
        OffsetDateTime endDate,

        @Schema(description = "Цена тура", example = "1000.0")
        Double price,

        @Schema(description = "Идентификатор страны", example = "1")
        Long countryId,

        @Schema(description = "Список вложений")
        @ValidFiles
        List<MultipartFile> attachments
) { }
