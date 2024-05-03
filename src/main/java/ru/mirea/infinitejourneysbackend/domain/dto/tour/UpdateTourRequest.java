package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidFiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "Запрос на обноление тура")
public record UpdateTourRequest(
        @Schema(description = "Название тура", example = "Обновленный тур")
        String title,

        @Schema(description = "Описание тура", example = "Описание обновленного тура")
        String description,

        @Schema(description = "Дата начала тура", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime startDate,

        @Schema(description = "Дата окончания тура", example = "2023-05-15T10:10:10.000+00:00")
        OffsetDateTime endDate,

        @Schema(description = "Идентификатор страны", example = "1")
        Long countryId,

        @Schema(description = "Список идентификаторов удаленных вложений")
        List<UUID> deletedAttachments,

        @Schema(description = "Список новых вложений")
        @ValidFiles
        List<MultipartFile> attachments
) { }
