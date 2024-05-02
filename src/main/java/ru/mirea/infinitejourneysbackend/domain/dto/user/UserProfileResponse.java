package ru.mirea.infinitejourneysbackend.domain.dto.user;

import ru.mirea.infinitejourneysbackend.domain.model.Gender;
import ru.mirea.infinitejourneysbackend.domain.model.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,

        String username,

        String email,

        OffsetDateTime bannedAt,

        Role role,

        Gender gender
) { }
