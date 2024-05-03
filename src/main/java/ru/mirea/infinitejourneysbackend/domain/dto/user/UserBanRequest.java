package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;

import java.util.UUID;

@Schema(description = "Запрос на бан пользователя")
public record UserBanRequest(
        @Schema(description = "Идентификатор пользователя", example = "00000000-0000-0000-0000-000000000000")
        UUID id,

        @Schema(description = "Количество дней до конца бана", example = "10")
        @Max(value = 36500, message = "Максимальное количество дней не должно быть больше 36500")
        Integer days,

        @Schema(description = "Количество часов до конца бана", example = "10")
        @Max(value = 23, message = "Максимальное количество часов не должно быть больше 23")
        Integer hours,

        @Schema(description = "Количество минут до конца бана", example = "10")
        @Max(value = 59, message = "Максимальное количество минут не должно быть больше 59")
        Integer minutes

) { }
