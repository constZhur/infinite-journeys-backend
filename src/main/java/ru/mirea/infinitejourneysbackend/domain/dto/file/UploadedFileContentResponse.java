package ru.mirea.infinitejourneysbackend.domain.dto.file;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Ответ с содержимым загруженного файла")
public record UploadedFileContentResponse(
        @Schema(description = "Имя файла", example = "filename")
        String name,

        @Schema(description = "Содержимое файла в виде массива байтов")
        byte[] content
) { }
