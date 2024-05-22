package ru.mirea.infinitejourneysbackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CommentFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CommentThreadFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.CreateCommentRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.comment.UpdateCommentRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Comment;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.comment.CommentNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.user.ForbiddenAccessProblem;
import ru.mirea.infinitejourneysbackend.repository.CommentRepository;
import ru.mirea.infinitejourneysbackend.service.CommentService;
import ru.mirea.infinitejourneysbackend.service.TourService;
import ru.mirea.infinitejourneysbackend.service.UserService;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository repository;

    @Mock
    private TourService tourService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    @Test
    @Transactional
    void testCreateComment() {
        CreateCommentRequest request = mock(CreateCommentRequest.class);
        Tour tour = mock(Tour.class);
        User user = mock(User.class);

        when(request.tourId()).thenReturn(1L);
        when(tourService.getById(1L)).thenReturn(tour);
        when(userService.getCurrentUser()).thenReturn(user);
        when(request.content()).thenReturn("Test content");
        when(request.isAnonymous()).thenReturn(false);
        when(request.answerToId()).thenReturn(null);

        Comment comment = new Comment();
        comment.setContent("Test content");
        comment.setTour(tour);
        comment.setAuthor(user);
        comment.setIsAnonymous(false);

        when(repository.save(any(Comment.class))).thenReturn(comment);

        Comment createdComment = commentService.create(request);

        verify(repository).save(any(Comment.class));
        assertThat(createdComment.getContent()).isEqualTo("Test content");
        assertThat(createdComment.getTour()).isEqualTo(tour);
        assertThat(createdComment.getAuthor()).isEqualTo(user);
        assertThat(createdComment.getIsAnonymous()).isEqualTo(false);
    }

    @Test
    void testGetById() {
        Comment comment = new Comment();
        comment.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(comment));

        Comment foundComment = commentService.getById(1L);

        assertThat(foundComment).isEqualTo(comment);
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundProblem.class, () -> commentService.getById(1L));
    }

    @Test
    void testUpdateComment() {
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Old content");
        comment.setAuthor(currentUser);

        UpdateCommentRequest request = mock(UpdateCommentRequest.class);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.getId()).thenReturn(userId);
        when(repository.findById(1L)).thenReturn(Optional.of(comment));
        when(request.content()).thenReturn("Updated content");

        when(repository.save(any(Comment.class))).thenReturn(comment);

        Comment updatedComment = commentService.update(request, 1L);

        assertThat(comment.getContent()).isEqualTo("Updated content");
        assertThat(comment.isContentModified()).isTrue();
        verify(repository).save(comment);
        assertThat(updatedComment).isEqualTo(comment);
    }

    @Test
    void testUpdateCommentForbidden() {
        UUID currentUserId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        User currentUser = mock(User.class);
        User anotherUser = mock(User.class);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Old content");
        comment.setAuthor(anotherUser);

        UpdateCommentRequest request = mock(UpdateCommentRequest.class);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.getId()).thenReturn(currentUserId);
        when(anotherUser.getId()).thenReturn(anotherUserId);
        when(repository.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(ForbiddenAccessProblem.class, () -> commentService.update(request, 1L));

        verify(repository, never()).save(any(Comment.class));
    }

    @Test
    @Transactional
    void testDeleteComment() {
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(currentUser);

        when(currentUser.getId()).thenReturn(userId);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(repository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteById(1L);

        verify(repository).delete(comment);
    }

    @Test
    void testDeleteCommentForbidden() {
        UUID currentUserId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        User currentUser = mock(User.class);
        User anotherUser = mock(User.class);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(anotherUser);

        when(currentUser.getId()).thenReturn(currentUserId);
        when(anotherUser.getId()).thenReturn(anotherUserId);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.isAdmin()).thenReturn(false);
        when(repository.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(ForbiddenAccessProblem.class, () -> commentService.deleteById(1L));

        verify(repository, never()).delete(any(Comment.class));
    }

    @Test
    void testFindByTourId() {
        CommentFilter filter = mock(CommentFilter.class);
        Pageable pageable = PageRequest.of(0, 10);
        Comment comment = new Comment();
        Page<Comment> page = new PageImpl<>(Collections.singletonList(comment), pageable, 1);

        when(filter.getTourId()).thenReturn(1L);
        when(filter.getPage()).thenReturn(0);
        when(filter.getSize()).thenReturn(10);
        when(repository.findAllByTourId(1L, pageable)).thenReturn(page);

        Page<Comment> result = commentService.findByTourId(filter);

        assertThat(result.getContent()).containsExactly(comment);
    }

    @Test
    void testFindByCommentId() {
        CommentThreadFilter filter = mock(CommentThreadFilter.class);
        Pageable pageable = PageRequest.of(0, 10);
        Comment comment = new Comment();
        Page<Comment> page = new PageImpl<>(Collections.singletonList(comment), pageable, 1);

        when(filter.getCommentId()).thenReturn(1L);
        when(filter.getPage()).thenReturn(0);
        when(filter.getSize()).thenReturn(10);
        when(repository.findAllByParentId(1L, pageable)).thenReturn(page);

        Page<Comment> result = commentService.findByCommentId(filter);

        assertThat(result.getContent()).containsExactly(comment);
    }

    @Test
    @Transactional
    void testDeleteByAuthorId() {
        UUID authorId = UUID.randomUUID();

        commentService.deleteByAuthorId(authorId);

        verify(repository).deleteAllByAuthorId(authorId);
    }
}

