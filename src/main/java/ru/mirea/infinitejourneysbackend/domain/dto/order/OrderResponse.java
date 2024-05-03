package ru.mirea.infinitejourneysbackend.domain.dto.order;

import java.time.OffsetDateTime;

public record OrderResponse(
    Long id,

    String tourTitle,

    String tourDescription,

    String countryName,

    Double price,

    String sellerName,

    OffsetDateTime createdAt
) { }
