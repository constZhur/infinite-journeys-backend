package ru.mirea.infinitejourneysbackend.domain.dto.file;


import lombok.Builder;

@Builder
public record UploadedFileContentResponse(
        String name,

        byte[] content
) { }
