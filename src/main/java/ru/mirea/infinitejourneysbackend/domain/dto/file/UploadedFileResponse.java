package ru.mirea.infinitejourneysbackend.domain.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Информация о загруженном файле")
public record UploadedFileResponse(
        @Schema(description = "Идентификатор файла", example = "00000000-0000-0000-0000-000000000000")
        UUID id,

        @Schema(description = "Оригинальное имя файла", example = "filename")
        String originalName,

        @Schema(description = "Расширение файла", example = "jpeg")
        String extension,

        @Schema(description = "Размер файла", example = "1024")
        Long size,

        @Schema(description = "Путь к файлу", example = "/some/path/")
        String path,

        @Schema(description = "Дата и время создания записи", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime createdAt,

        @Schema(description = "Дата и время последнего обновления записи", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime updatedAt
) { }
