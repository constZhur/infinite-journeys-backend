package ru.mirea.infinitejourneysbackend.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на аутентификацию")
public record SignInRequest(
        @Schema(description = "Имя пользователя", example = "Wenos")
        @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
        @NotBlank(message = "Имя пользователя не может быть пустыми")
        String username,

        @Schema(description = "Пароль", example = "secret123")
        @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
        @NotBlank(message = "Пароль не может быть пустыми")
        String password
) { }
