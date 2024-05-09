package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidGender;

@Schema(description = "Запрос на обновление пола пользователя")
public record UpdateUserGenderRequest(
        @Schema(description = "Пол пользователя", example = "MALE")
        @ValidGender(message = "Значение пола недопустимо")
        @NotBlank(message = "Пол является обязательным параметром")
        String gender
) { }
