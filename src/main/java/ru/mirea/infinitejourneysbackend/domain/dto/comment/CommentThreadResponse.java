package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;

import java.time.OffsetDateTime;

@Schema(description = "Ответ на запрос комментария в треде")
public record CommentThreadResponse(
        @Schema(description = "Идентификатор комментария", example = "1")
        Long id,

        @Schema(description = "Текст комментария", example = "Комментарий")
        String content,

        @Schema(description = "Идентификатор связанного поста", example = "1")
        Long tourId,

        @Schema(description = "Идентификатор начала треда")
        Long parentId,

        @Schema(description = "Идентификатор комментария для ответа")
        CommentResponse answerTo,

        @Schema(description = "Время загрузки файла", example = "2023-05-05T10:10:10.000+00:00")
        OffsetDateTime createdAt,

        @Schema(description = "Время обновления файла", example = "2024-05-05T10:10:10.000+00:00")
        OffsetDateTime updatedAt,

        @Schema(description = "Автора комментария")
        UserResponse author,

        @Schema(description = "Был ли комментарий изменен", example = "false")
        Boolean isUpdated
) { }
