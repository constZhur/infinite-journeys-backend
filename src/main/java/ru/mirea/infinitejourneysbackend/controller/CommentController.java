package ru.mirea.infinitejourneysbackend.controller;

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
public class CommentController {
    private final CommentService service;
    private final CommentMapper mapper;

    @PostMapping
    public CommentResponse createComment(@RequestBody @Valid CreateCommentRequest request) {
        Comment comment = service.create(request);
        return mapper.toResponse(comment);
    }

    @GetMapping("/{commentId}")
    public CommentResponse getCommentById(@PathVariable Long commentId) {
        Comment comment = service.getById(commentId);
        return mapper.toResponse(comment);
    }

    @PutMapping("/{commentId}")
    public CommentResponse updateComment(
            @RequestBody @Valid UpdateCommentRequest request,
            @PathVariable Long commentId
    ) {
        Comment comment = service.update(request, commentId);
        return mapper.toResponse(comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        service.deleteById(commentId);
    }

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
