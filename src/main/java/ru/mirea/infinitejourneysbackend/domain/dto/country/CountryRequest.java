package ru.mirea.infinitejourneysbackend.domain.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CountryRequest(
        @Size(min = 2, max = 50, message = "Название должно быть от 2 до 50 символов")
        @NotBlank(message = "Название не должно быть пустым")
        String name
) { }
