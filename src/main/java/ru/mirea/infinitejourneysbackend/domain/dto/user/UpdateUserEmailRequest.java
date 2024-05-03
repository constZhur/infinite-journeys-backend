package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на обновление почты пользователя")
public record UpdateUserEmailRequest(
        @Schema(description = "Новыйа дрес электронной почты", example = "newwenos@gmail.com")
        @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
        @NotBlank(message = "Адрес электронной почты не может быть пустыми")
        @Email(message = "Email адрес должен быть в формате user@example.com")
        String email
) { }
