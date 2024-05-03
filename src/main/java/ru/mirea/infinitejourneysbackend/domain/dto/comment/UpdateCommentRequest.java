package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на обновление комментария")
public record UpdateCommentRequest(
        @Schema(description = "Текст комментария", example = "Комментарий")
        @NotBlank(message = "Текст комментария не должен быть пустым")
        @Size(min = 2, max = 999, message = "Текст комментария должен быть от 5 до 999 символов")
        String content
) { }
