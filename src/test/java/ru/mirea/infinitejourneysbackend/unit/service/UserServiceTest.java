package ru.mirea.infinitejourneysbackend.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mirea.infinitejourneysbackend.config.security.SuperUserConfig;
import ru.mirea.infinitejourneysbackend.domain.dto.user.*;
import ru.mirea.infinitejourneysbackend.domain.model.Gender;
import ru.mirea.infinitejourneysbackend.domain.model.Role;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.user.*;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;
import ru.mirea.infinitejourneysbackend.service.UserService;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SuperUserConfig superUserConfig;

    @InjectMocks
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private final UUID userId = UUID.randomUUID();
    private final UUID superUserId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void createUser_Success() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("testuser@gmail.com")
                .build();

        when(repository.existsByUsername(anyString())).thenReturn(false);
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertThat(createdUser).isEqualTo(user);
        verify(repository).save(user);
    }

    @Test
    void createUser_DuplicateUsername() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("testuser@gmail.com")
                .build();

        when(repository.existsByUsername(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(UserNotUniqueUsernameProblem.class);

        verify(repository, never()).save(user);
    }

    @Test
    void createUser_DuplicateEmail() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("testuser@gmail.com")
                .build();

        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(UserNotUniqueEmailProblem.class);

        verify(repository, never()).save(user);
    }

    @Test
    void getCurrentUser_Success() {
        User user = User.builder().username("testuser").build();
        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser();

        assertThat(currentUser).isEqualTo(user);
        verify(repository).findByUsername("testuser");
    }

    @Test
    void getCurrentUser_NotFound() {
        when(authentication.getName()).thenReturn("unknownuser");
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentUser())
                .isInstanceOf(UserNotFoundProblem.class);
    }

    @Test
    void setBannedAt_Success() {
        User user = User.builder().id(userId).role(Role.ROLE_BUYER).build();
        User currentUser = User.builder().id(superUserId).role(Role.ROLE_ADMIN).build();
        UserBanRequest request = new UserBanRequest(userId, 1, 0, 0);

        when(authentication.getName()).thenReturn("admin");
        when(repository.findByUsername("admin")).thenReturn(Optional.of(currentUser));
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(superUserConfig.isSuperuser(currentUser.getId())).thenReturn(true);

        userService.setBannedAt(request);

        assertThat(user.getBannedAt()).isNotNull();
        verify(repository).save(user);
    }

    @Test
    void setBannedAt_ForbiddenAccess() {
        User user = User.builder().id(userId).role(Role.ROLE_ADMIN).build();
        User currentUser = User.builder().id(superUserId).role(Role.ROLE_BUYER).build();
        UserBanRequest request = new UserBanRequest(userId, 1, 0, 0);

        when(authentication.getName()).thenReturn("buyer");
        when(repository.findByUsername("buyer")).thenReturn(Optional.of(currentUser));
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(superUserConfig.isSuperuser(currentUser.getId())).thenReturn(false);

        assertThatThrownBy(() -> userService.setBannedAt(request))
                .isInstanceOf(ForbiddenAccessProblem.class);

        verify(repository, never()).save(user);
    }

    @Test
    void changePassword_Success() {
        User currentUser = User.builder().id(userId).password("oldPassword").build();
        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest("oldPassword", "newPassword");

        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        userService.changePassword(request);

        assertThat(currentUser.getPassword()).isEqualTo("encodedNewPassword");
        verify(repository).save(currentUser);
    }

    @Test
    void changePassword_InvalidOldPassword() {
        User currentUser = User.builder().id(userId).password("oldPassword").build();
        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest("invalidOldPassword", "newPassword");

        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(request))
                .isInstanceOf(InvalidUserPasswordProblem.class);

        verify(repository, never()).save(currentUser);
    }

    @Test
    void changeRole_Success() {
        User currentUser = User.builder().id(superUserId).role(Role.ROLE_ADMIN).build();
        User user = User.builder().id(userId).role(Role.ROLE_BUYER).build();
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(userId, "ROLE_SELLER");

        when(authentication.getName()).thenReturn("admin");
        when(repository.findByUsername("admin")).thenReturn(Optional.of(currentUser));
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(superUserConfig.isSuperuser(currentUser.getId())).thenReturn(true);

        userService.changeRole(request);

        assertThat(user.getRole()).isEqualTo(Role.ROLE_SELLER);
        verify(repository).save(user);
    }

    @Test
    void changeRole_ForbiddenAccess() {
        User currentUser = User.builder().id(userId).role(Role.ROLE_BUYER).build();
        User user = User.builder().id(superUserId).role(Role.ROLE_ADMIN).build();
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(superUserId, "ROLE_BUYER");

        when(authentication.getName()).thenReturn("buyer");
        when(repository.findByUsername("buyer")).thenReturn(Optional.of(currentUser));
        when(repository.findById(superUserId)).thenReturn(Optional.of(user));
        when(superUserConfig.isSuperuser(currentUser.getId())).thenReturn(false);

        assertThatThrownBy(() -> userService.changeRole(request))
                .isInstanceOf(ForbiddenAccessProblem.class);

        verify(repository, never()).save(user);
    }

    @Test
    void initSuperUser_CreatesSuperUser() {
        when(superUserConfig.isSuperuserEnabled()).thenReturn(true);
        when(superUserConfig.getSuperuserId()).thenReturn(superUserId);
        when(repository.findById(superUserId)).thenReturn(Optional.empty());

        userService.initSuperUser();

        verify(repository).save(argThat(user ->
                user.getId().equals(superUserId) &&
                        user.getUsername().equals("superuser") &&
                        user.getRole().equals(Role.ROLE_ADMIN)
        ));
    }

    @Test
    void initSuperUser_UpdatesExistingSuperUser() {
        User existingUser = User.builder().id(superUserId).build();
        when(superUserConfig.isSuperuserEnabled()).thenReturn(true);
        when(superUserConfig.getSuperuserId()).thenReturn(superUserId);
        when(repository.findById(superUserId)).thenReturn(Optional.of(existingUser));

        userService.initSuperUser();

        assertThat(existingUser.getRole()).isEqualTo(Role.ROLE_ADMIN);
        verify(repository).save(existingUser);
    }

    @Test
    void getByUsername_UserDeleted() {
        User user = User.builder().username("testuser").deletedAt(OffsetDateTime.now()).build();
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.getByUsername("testuser"))
                .isInstanceOf(UserDeletedProblem.class);
    }

    @Test
    void resetBannedAt_Success() {
        User currentUser = User.builder().id(superUserId).role(Role.ROLE_ADMIN).build();
        User user = User.builder().id(userId).role(Role.ROLE_BUYER).bannedAt(OffsetDateTime.now()).build();

        when(authentication.getName()).thenReturn("admin");
        when(repository.findByUsername("admin")).thenReturn(Optional.of(currentUser));
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(superUserConfig.isSuperuser(currentUser.getId())).thenReturn(true);

        userService.resetBannedAt(userId);

        assertThat(user.getBannedAt()).isNull();
        verify(repository).save(user);
    }

    @Test
    void selfUpdateUsername_Success() {
        User currentUser = User.builder().id(userId).username("oldUsername").build();
        UpdateUserUsernameRequest request = new UpdateUserUsernameRequest("newUsername");

        when(authentication.getName()).thenReturn("oldUsername");
        when(repository.findByUsername("oldUsername")).thenReturn(Optional.of(currentUser));
        when(repository.existsByUsername("newUsername")).thenReturn(false);

        userService.selfUpdateUsername(request);

        assertThat(currentUser.getUsername()).isEqualTo("newUsername");
        verify(repository).save(currentUser);
    }

    @Test
    void selfUpdateUsername_DuplicateUsername() {
        User currentUser = User.builder().id(userId).username("oldUsername").build();
        UpdateUserUsernameRequest request = new UpdateUserUsernameRequest("newUsername");

        when(authentication.getName()).thenReturn("oldUsername");
        when(repository.findByUsername("oldUsername")).thenReturn(Optional.of(currentUser));
        when(repository.existsByUsername("newUsername")).thenReturn(true);

        assertThatThrownBy(() -> userService.selfUpdateUsername(request))
                .isInstanceOf(UserNotUniqueUsernameProblem.class);

        verify(repository, never()).save(currentUser);
    }

    @Test
    void selfUpdateEmail_Success() {
        User currentUser = User.builder().id(userId).email("oldEmail@gmail.com").build();
        UpdateUserEmailRequest request = new UpdateUserEmailRequest("newEmail@gmail.com");

        when(authentication.getName()).thenReturn("oldEmail@gmail.com");
        when(repository.findByUsername("oldEmail@gmail.com")).thenReturn(Optional.of(currentUser));
        when(repository.existsByEmail("newEmail@gmail.com")).thenReturn(false);

        userService.selfUpdateEmail(request);

        assertThat(currentUser.getEmail()).isEqualTo("newEmail@gmail.com");
        verify(repository).save(currentUser);
    }

    @Test
    void selfUpdateEmail_DuplicateEmail() {
        User currentUser = User.builder().id(userId).email("oldEmail@gmail.com").build();
        UpdateUserEmailRequest request = new UpdateUserEmailRequest("newEmail@gmail.com");

        when(authentication.getName()).thenReturn("oldEmail@gmail.com");
        when(repository.findByUsername("oldEmail@gmail.com")).thenReturn(Optional.of(currentUser));
        when(repository.existsByEmail("newEmail@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.selfUpdateEmail(request))
                .isInstanceOf(UserNotUniqueEmailProblem.class);

        verify(repository, never()).save(currentUser);
    }

    @Test
    void findByFilter_Success() {
        UserFilter filter = new UserFilter();
        filter.setPage(0);
        filter.setSize(10);

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> expectedPage = new PageImpl<>(Collections.singletonList(User.builder().id(userId).build()));

        when(repository.findAllWithFilter(any(), any(), any(), any(), any(), any(), any())).thenReturn(expectedPage);

        Page<User> resultPage = userService.findByFilter(filter);

        assertThat(resultPage).isEqualTo(expectedPage);
        verify(repository).findAllWithFilter(
                any(), any(), any(), any(), any(), any(), eq(pageable)
        );
    }

    @Test
    void isSuperuser_Success() {
        User currentUser = User.builder().id(superUserId).build();

        when(authentication.getName()).thenReturn("superuser");
        when(repository.findByUsername("superuser")).thenReturn(Optional.of(currentUser));
        when(superUserConfig.isSuperuser(superUserId)).thenReturn(true);

        boolean isSuperuser = userService.isSuperuser();

        assertThat(isSuperuser).isTrue();
    }

    @Test
    void getCurrentBalance_Success() {
        User currentUser = User.builder().id(userId).balance(1000.0).build();

        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));

        Double balance = userService.getCurrentBalance();

        assertThat(balance).isEqualTo(1000.0);
    }

    @Test
    void topUpBalance_Success() {
        User currentUser = User.builder().id(userId).balance(1000.0).build();
        UpdateBalanceRequest request = new UpdateBalanceRequest(500.0);

        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));

        userService.topUpBalance(request);

        assertThat(currentUser.getBalance()).isEqualTo(1500.0);
        verify(repository).save(currentUser);
    }

    @Test
    void withdrawFromBalance_Success() {
        User currentUser = User.builder().id(userId).balance(1000.0).build();
        UpdateBalanceRequest request = new UpdateBalanceRequest(500.0);

        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));

        userService.withdrawFromBalance(request);

        assertThat(currentUser.getBalance()).isEqualTo(500.0);
        verify(repository).save(currentUser);
    }

    @Test
    void withdrawFromBalance_InsufficientBalance() {
        User currentUser = User.builder().id(userId).balance(1000.0).build();
        UpdateBalanceRequest request = new UpdateBalanceRequest(1500.0);

        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));

        assertThatThrownBy(() -> userService.withdrawFromBalance(request))
                .isInstanceOf(InsufficientBalanceProblem.class);

        verify(repository, never()).save(currentUser);
    }
}
