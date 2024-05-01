package ru.mirea.infinitejourneysbackend.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserUsernameRequest(
        @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
        @Pattern(regexp = ".*[a-zA-Z].*", message = "В имени быть хотя бы одна буква")
        @NotBlank(message = "Имя пользователя не может быть пустыми")
        String username
) { }
