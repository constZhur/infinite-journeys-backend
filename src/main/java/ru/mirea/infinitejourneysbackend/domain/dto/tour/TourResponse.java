package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.file.UploadedFileResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;

import java.time.OffsetDateTime;
import java.util.List;

public record TourResponse(
        Long id,

        String title,

        String description,

        CountryResponse country,

        UserResponse seller,

        List<UploadedFileResponse> attachments,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt
) { }
