package ru.mirea.infinitejourneysbackend.domain.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignUpRequest(
        @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
        @Pattern(regexp = ".*[a-zA-Z0-9_].*", message = "В имени быть хотя бы одна буква")
        @NotBlank(message = "Имя пользователя не может быть пустыми")
        String username,

        @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
        @NotBlank(message = "Адрес электронной почты не может быть пустыми")
        @Email(message = "Email адрес должен быть в формате user@example.com")
        String email,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "Пароль должен содержать как минимум одну букву и одну цифру, и быть длиной не менее 8 символов")
        @Size(max = 255, message = "Длина пароля должна быть больше 255 символов")
        String password
) { }
