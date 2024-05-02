package ru.mirea.infinitejourneysbackend.domain.dto.user;

import java.util.UUID;

public record DeleteUserRequest(
        UUID userId,

        boolean deleteComments
) { }
