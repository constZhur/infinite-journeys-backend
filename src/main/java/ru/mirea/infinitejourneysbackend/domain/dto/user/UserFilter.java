package ru.mirea.infinitejourneysbackend.domain.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidRole;

import java.util.UUID;

@Data
public class UserFilter extends PageRequestDTO {
    private UUID id;

    @Size(max = 50, message = "Имя пользователя должно содержать до 50 символов")
    private String username;

    @Size(max = 255, message = "Адрес электронной почты должен до 255 символов")
    private String email;

    private Boolean isBanned;

    @ValidRole(nullable = true, message = "Значение роли недопустимо")
    private String role;
}

