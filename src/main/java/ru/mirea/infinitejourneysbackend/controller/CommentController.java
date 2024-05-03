package ru.mirea.infinitejourneysbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.*;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageResponse;
import ru.mirea.infinitejourneysbackend.domain.model.Comment;
import ru.mirea.infinitejourneysbackend.mapper.CommentMapper;
import ru.mirea.infinitejourneysbackend.service.CommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Контроллер комментариев", description = "Управление комментариями")
@SecurityRequirement(name = "infinite-journeys-api")
public class CommentController {
    private final CommentService service;
    private final CommentMapper mapper;

    @Operation(summary = "Создание комментария", description = "Создает новый комментарий.")
    @PostMapping
    public CommentResponse createComment(@RequestBody @Valid CreateCommentRequest request) {
        Comment comment = service.create(request);
        return mapper.toResponse(comment);
    }

    @Operation(summary = "Получение комментария по ID", description = "Получает информацию о комментарии по его идентификатору.")
    @GetMapping("/{commentId}")
    public CommentResponse getCommentById(@PathVariable Long commentId) {
        Comment comment = service.getById(commentId);
        return mapper.toResponse(comment);
    }

    @Operation(summary = "Обновление комментария", description = "Обновляет информацию о существующем комментарии.")
    @PutMapping("/{commentId}")
    public CommentResponse updateComment(
            @RequestBody @Valid UpdateCommentRequest request,
            @PathVariable Long commentId
    ) {
        Comment comment = service.update(request, commentId);
        return mapper.toResponse(comment);
    }

    @Operation(summary = "Удаление комментария по ID", description = "Удаляет комментарий по его идентификатору.")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        service.deleteById(commentId);
    }

    @Operation(summary = "Поиск комментариев по ID тура", description = "Позволяет найти комментарии по идентификатору тура.")
    @PostMapping("/tour")
    public PageResponse<CommentResponse> findCommentsByTourId(@RequestBody @Valid CommentFilter filter) {
        var comments = service.findByTourId(filter);

        var result = new PageResponse<CommentResponse>();
        result.setTotalPages(comments.getTotalPages());
        result.setTotalSize(comments.getTotalElements());
        result.setPageNumber(comments.getNumber());
        result.setPageSize(comments.getSize());
        result.setContent(mapper.toResponse(comments.getContent()));
        return result;
    }

    @Operation(summary = "Поиск комментариев в потоке", description = "Позволяет найти комментарии в определенном потоке.")
    @PostMapping("/thread")
    public PageResponse<CommentThreadResponse> findThreadComments(@RequestBody @Valid CommentThreadFilter filter) {
        var comments = service.findByCommentId(filter);

        var result = new PageResponse<CommentThreadResponse>();
        result.setTotalPages(comments.getTotalPages());
        result.setTotalSize(comments.getTotalElements());
        result.setPageNumber(comments.getNumber());
        result.setPageSize(comments.getSize());
        result.setContent(mapper.toResponseThread(comments.getContent()));
        return result;
    }
}

