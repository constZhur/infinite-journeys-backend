package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление пароля пользователя")
public record UpdateUserPasswordRequest(
        @Schema(description = "Старый пароль", example = "secret123")
        @Size(max = 255, message = "Длина пароля должна быть не больше 255 символов")
        String oldPassword,

        @Schema(description = "Новый пароль", example = "newsecret123")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "Пароль должен содержать как минимум одну букву и одну цифру, и быть длиной не менее 8 символов")
        @Size(max = 255, message = "Длина пароля должна быть не больше 255 символов")
        String newPassword
) { }
