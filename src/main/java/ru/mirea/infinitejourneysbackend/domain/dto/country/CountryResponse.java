package ru.mirea.infinitejourneysbackend.domain.dto.country;

import java.time.OffsetDateTime;

public record CountryResponse(
        Long id,

        String name,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt
) { }
