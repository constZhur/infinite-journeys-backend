package ru.mirea.infinitejourneysbackend.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidRole;

import java.util.UUID;

public record UpdateUserRoleRequest(
        UUID id,

        @ValidRole(message = "Значение роли недопустимо")
        @NotBlank(message = "Роль является обязательным параметром")
        String role
) { }
