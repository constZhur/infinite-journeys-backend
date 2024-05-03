package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Запрос на удаление пользователя")
public record DeleteUserRequest(
        @Schema(description = "Идентификатор пользователя", example = "00000000-0000-0000-0000-000000000000")
        UUID userId,

        @Schema(description = "Удаляются ли комментарии пользователя")
        boolean deleteComments
) { }
