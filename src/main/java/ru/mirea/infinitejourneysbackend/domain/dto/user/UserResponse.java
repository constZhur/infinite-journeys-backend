package ru.mirea.infinitejourneysbackend.domain.dto.user;

import lombok.Builder;
import ru.mirea.infinitejourneysbackend.domain.model.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,

        String username,

        String email,

        OffsetDateTime bannedAt,

        Role role,

        OffsetDateTime deletedAt
) { }
