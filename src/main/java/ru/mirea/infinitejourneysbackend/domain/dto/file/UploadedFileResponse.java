package ru.mirea.infinitejourneysbackend.domain.dto.file;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record UploadedFileResponse(
        UUID id,

        String originalName,

        String extension,

        Long size,

        String path,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt
) { }
