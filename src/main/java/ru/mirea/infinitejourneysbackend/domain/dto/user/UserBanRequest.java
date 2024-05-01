package ru.mirea.infinitejourneysbackend.domain.dto.user;

import jakarta.validation.constraints.Max;

import java.util.UUID;

public record UserBanRequest(
        UUID id,

        @Max(value = 36500, message = "Максимальное количество дней не должно быть больше 36500")
        Integer days,

        @Max(value = 23, message = "Максимальное количество часов не должно быть больше 23")
        Integer hours,

        @Max(value = 59, message = "Максимальное количество минут не должно быть больше 59")
        Integer minutes

) { }
