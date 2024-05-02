package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;

import java.time.OffsetDateTime;

public record CommentThreadResponse(
        Long id,

        String content,

        Long tourId,

        Long parentId,

        CommentResponse answerTo,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt,

        UserResponse author,

        Boolean isUpdated
) { }
