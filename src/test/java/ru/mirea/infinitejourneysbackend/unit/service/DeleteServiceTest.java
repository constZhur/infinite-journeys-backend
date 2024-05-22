package ru.mirea.infinitejourneysbackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mirea.infinitejourneysbackend.config.security.SuperUserConfig;
import ru.mirea.infinitejourneysbackend.domain.dto.user.DeleteUserRequest;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.user.ForbiddenAccessProblem;
import ru.mirea.infinitejourneysbackend.service.CommentService;
import ru.mirea.infinitejourneysbackend.service.DeleteService;
import ru.mirea.infinitejourneysbackend.service.TourService;
import ru.mirea.infinitejourneysbackend.service.UserService;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TourService tourService;

    @Mock
    private CommentService commentService;

    @Mock
    private SuperUserConfig superUserConfig;

    @InjectMocks
    private DeleteService deleteService;

    @Test
    void testDeleteUserSelf() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getById(userId)).thenReturn(user);
        when(user.getId()).thenReturn(userId);

        DeleteUserRequest request = mock(DeleteUserRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.deleteComments()).thenReturn(true);

        deleteService.deleteUser(request);

        verify(commentService).deleteByAuthorId(userId);
        verify(tourService).deleteBySellerId(userId);
        verify(user).setComments(null);
        verify(user).setTours(null);
        verify(user).setDeletedAt(any(OffsetDateTime.class));
        verify(userService).save(user);
    }

    @Test
    void testDeleteUserByAdmin() {
        UUID userId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        User admin = mock(User.class);
        User targetUser = mock(User.class);

        when(userService.getCurrentUser()).thenReturn(admin);
        when(userService.getById(targetUserId)).thenReturn(targetUser);
        when(admin.getId()).thenReturn(userId);
        when(admin.isAdmin()).thenReturn(true);
        when(targetUser.getId()).thenReturn(targetUserId);
        when(userService.hasAccessToUser(admin, targetUser)).thenReturn(true);

        DeleteUserRequest request = mock(DeleteUserRequest.class);
        when(request.userId()).thenReturn(targetUserId);
        when(request.deleteComments()).thenReturn(true);

        deleteService.deleteUser(request);

        verify(commentService).deleteByAuthorId(targetUserId);
        verify(tourService).deleteBySellerId(targetUserId);
        verify(targetUser).setComments(null);
        verify(targetUser).setTours(null);
        verify(targetUser).setDeletedAt(any(OffsetDateTime.class));
        verify(userService).save(targetUser);
    }

    @Test
    void testDeleteUserForbiddenByNonAdmin() {
        UUID userId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        User user = mock(User.class);
        User targetUser = mock(User.class);
        DeleteUserRequest request = mock(DeleteUserRequest.class);

        lenient().when(userService.getCurrentUser()).thenReturn(user);
        lenient().when(user.getId()).thenReturn(userId);
        lenient().when(user.isAdmin()).thenReturn(false);
        lenient().when(userService.getById(targetUserId)).thenReturn(targetUser);
        lenient().when(request.userId()).thenReturn(targetUserId);
        lenient().when(request.deleteComments()).thenReturn(true);

        assertThatThrownBy(() -> deleteService.deleteUser(request))
                .isInstanceOf(ForbiddenAccessProblem.class);

        verify(commentService, never()).deleteByAuthorId(any());
        verify(tourService, never()).deleteBySellerId(any());
        verify(userService, never()).save(any());
    }


    @Test
    void testDeleteSuperuser() {
        UUID superUserId = UUID.randomUUID();
        User superUser = mock(User.class);

        when(userService.getCurrentUser()).thenReturn(superUser);
        when(userService.getById(superUserId)).thenReturn(superUser);
        when(superUser.getId()).thenReturn(superUserId);
        when(superUserConfig.isSuperuser(superUserId)).thenReturn(true);

        DeleteUserRequest request = mock(DeleteUserRequest.class);
        when(request.userId()).thenReturn(superUserId);

        assertThatThrownBy(() -> deleteService.deleteUser(request))
                .isInstanceOf(ForbiddenAccessProblem.class);

        verify(commentService, never()).deleteByAuthorId(any());
        verify(tourService, never()).deleteBySellerId(any());
        verify(userService, never()).save(any());
    }

    @Test
    void testDeleteUserWithoutDeletingComments() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getById(userId)).thenReturn(user);
        when(user.getId()).thenReturn(userId);

        DeleteUserRequest request = mock(DeleteUserRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.deleteComments()).thenReturn(false);

        deleteService.deleteUser(request);

        verify(commentService, never()).deleteByAuthorId(userId);
        verify(tourService).deleteBySellerId(userId);
        verify(user, never()).setComments(null);
        verify(user).setTours(null);
        verify(user).setDeletedAt(any(OffsetDateTime.class));
        verify(userService).save(user);
    }
}
