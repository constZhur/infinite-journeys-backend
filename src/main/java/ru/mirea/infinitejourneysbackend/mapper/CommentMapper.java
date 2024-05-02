package ru.mirea.infinitejourneysbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CommentResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CommentThreadResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;
import ru.mirea.infinitejourneysbackend.domain.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TourMapper.class})
public interface CommentMapper {

    @Mapping(target = "tourId", source = "tour.id")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "answerToId", source = "answerTo.id")
    @Mapping(target = "author", source = "comment", qualifiedByName = "checkAnonymous")
    @Mapping(target = "isUpdated", source = "contentModified")
    CommentResponse toResponse(Comment comment);


    @Mapping(target = "tourId", source = "tour.id")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "author", source = "comment", qualifiedByName = "checkAnonymous")
    @Mapping(target = "isUpdated", source = "contentModified")
    CommentThreadResponse toResponseThread(Comment comment);



    @Named("checkAnonymous")
    default UserResponse checkAnonymous(Comment comment) {
        return comment.getIsAnonymous() ? UserResponse.builder().build() : UserResponse.builder()
                .id(comment.getAuthor().getId())
                .username(comment.getAuthor().getUsername())
                .email(comment.getAuthor().getEmail())
                .role(comment.getAuthor().getRole())
                .bannedAt(comment.getAuthor().getBannedAt())
                .build();
    }

    List<CommentResponse> toResponse(List<Comment> comments);
    List<CommentThreadResponse> toResponseThread(List<Comment> comments);
}

