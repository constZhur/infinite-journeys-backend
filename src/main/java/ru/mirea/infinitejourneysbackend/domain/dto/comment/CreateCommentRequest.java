package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateCommentRequest(
        @NotBlank(message = "Текст комментария не должен быть пустым")
        @Size(min = 1, max = 999, message = "Текст комментария должен быть от 1 до 999 символов")
        String content,

        @NotNull(message = "Id поста не должен быть пустым")
        Long tourId,

        Long answerToId,

        boolean isAnonymous
) { }
