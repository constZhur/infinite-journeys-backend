package ru.mirea.infinitejourneysbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CommentFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CommentThreadFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CreateCommentRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.UpdateCommentRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Comment;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.comment.CommentNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.user.ForbiddenAccessProblem;
import ru.mirea.infinitejourneysbackend.repository.CommentRepository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final TourService tourService;
    private final UserService userService;

    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    @Transactional
    public Comment create(CreateCommentRequest request) {
        var tour = tourService.getById(request.tourId());
        var user = userService.getCurrentUser();

        Comment answerTo = null;
        Comment parent = null;

        if (!isNull(request.answerToId())) {
            answerTo = getById(request.answerToId());
            parent = isNull(answerTo.getParent()) ? answerTo : answerTo.getParent();
        }

        return save(
                Comment.builder()
                        .content(request.content())
                        .parent(parent)
                        .answerTo(answerTo)
                        .tour(tour)
                        .author(user)
                        .isAnonymous(request.isAnonymous())
                        .build()
        );
    }

    public Optional<Comment> findById(Long id) {
        return repository.findById(id);
    }

    public Comment getById(Long commentId) {
        return findById(commentId).orElseThrow(() -> new CommentNotFoundProblem(commentId.toString()));
    }

    public Comment update(UpdateCommentRequest request, Long commentId) {
        User currentUser = userService.getCurrentUser();
        Comment comment = getById(commentId);

        if (!comment.isAuthor(currentUser)) {
            throw new ForbiddenAccessProblem();
        }

        comment.setContent(request.content());
        comment.setContentModified(true);
        return save(comment);
    }

    public void deleteById(Long commentId) {
        User currentUser = userService.getCurrentUser();
        Comment comment = getById(commentId);

        if (!comment.isAuthor(currentUser) && !currentUser.isAdmin()) {
            throw new ForbiddenAccessProblem();
        }
        repository.delete(comment);
    }

    public Page<Comment> findByTourId(CommentFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        return repository.findAllByTourId(filter.getTourId(), pageable);
    }

    public Page<Comment> findByCommentId(CommentThreadFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        return repository.findAllByParentId(filter.getCommentId(), pageable);
    }

    @Transactional
    public void deleteByAuthorId(UUID id) {
        repository.deleteAllByAuthorId(id);
    }

}
