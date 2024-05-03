package ru.mirea.infinitejourneysbackend.domain.dto.country;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на создание страны")
public record CountryRequest(
        @Schema(description = "Название страны", example = "Россия")
        @Size(min = 2, max = 50, message = "Название должно быть от 2 до 50 символов")
        @NotBlank(message = "Название не должно быть пустым")
        String name
) { }
