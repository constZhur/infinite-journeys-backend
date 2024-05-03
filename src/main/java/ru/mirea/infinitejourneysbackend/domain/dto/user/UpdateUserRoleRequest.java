package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidRole;

import java.util.UUID;

@Schema(description = "Запрос на обновление роли пользователя")
public record UpdateUserRoleRequest(
        @Schema(description = "Идентификатор пользователя", example = "00000000-0000-0000-0000-000000000000")
        UUID id,

        @Schema(description = "Роль пользователя", example = "ROLE_SELLER")
        @ValidRole(message = "Значение роли недопустимо")
        @NotBlank(message = "Роль является обязательным параметром")
        String role
) { }
