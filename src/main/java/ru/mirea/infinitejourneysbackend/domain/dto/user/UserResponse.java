package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ru.mirea.infinitejourneysbackend.domain.model.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        @Schema(description = "Идентификатор пользователя", example = "00000000-0000-0000-0000-000000000000")
        UUID id,

        @Schema(description = "Логин пользователя", example = "Wenos")
        String username,

        @Schema(description = "Почта пользователя", example = "wenos@gmail.com")
        String email,

        @Schema(description = "Дата бана", example = "2023-05-15T10:10:10.000+00:00")
        OffsetDateTime bannedAt,

        @Schema(description = "Роль пользователя", example = "ROLE_SELLER")
        Role role,

        @Schema(description = "Дата бана", example = "2023-05-11T10:10:10.000+00:00")
        OffsetDateTime deletedAt
) { }
