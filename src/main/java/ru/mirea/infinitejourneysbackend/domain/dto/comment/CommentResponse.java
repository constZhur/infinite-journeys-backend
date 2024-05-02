package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;

import java.time.OffsetDateTime;

public record CommentResponse(
        Long id,

        String content,

        Long tourId,

        Long parentId,

        Long answerToId,

        OffsetDateTime createdAt,

        OffsetDateTime updatedAt,

        UserResponse author,

        Boolean isUpdated

) { }
