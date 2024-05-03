package ru.mirea.infinitejourneysbackend.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidRole;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Запрос пользователей по фильтру")
public class UserFilter extends PageRequestDTO {
    @Schema(description = "Идентификатор пользователя", example = "00000000-0000-0000-0000-000000000000")
    private UUID id;

    @Schema(description = "Логин пользователя", example = "Wenos")
    @Size(max = 50, message = "Имя пользователя должно содержать до 50 символов")
    private String username;

    @Schema(description = "Почта пользователя", example = "wenos@gmail.com")
    @Size(max = 255, message = "Адрес электронной почты должен до 255 символов")
    private String email;

    @Schema(description = "Признак блокировки пользователя", example = "false")
    private Boolean isBanned;

    @Schema(description = "Роль пользователя", example = "ROLE_SELLER")
    @ValidRole(nullable = true, message = "Значение роли недопустимо")
    private String role;
}

