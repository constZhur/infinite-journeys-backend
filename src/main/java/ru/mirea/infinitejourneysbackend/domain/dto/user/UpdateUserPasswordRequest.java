package ru.mirea.infinitejourneysbackend.domain.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserPasswordRequest(
        @Size(max = 255, message = "Длина пароля должна быть не больше 255 символов")
        String oldPassword,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "Пароль должен содержать как минимум одну букву и одну цифру, и быть длиной не менее 8 символов")
        @Size(max = 255, message = "Длина пароля должна быть не больше 255 символов")
        String newPassword
) { }
