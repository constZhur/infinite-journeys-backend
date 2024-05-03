package ru.mirea.infinitejourneysbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.user.*;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.mapper.UserMapper;
import ru.mirea.infinitejourneysbackend.service.DeleteService;
import ru.mirea.infinitejourneysbackend.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final DeleteService deleteService;
    private final UserMapper mapper;

    @PatchMapping("/change-username")
    public void selfUpdateUsername(@RequestBody @Valid UpdateUserUsernameRequest request) {
        service.selfUpdateUsername(request);
    }

    @PatchMapping("/change-email")
    public void selfUpdateEmail(@RequestBody @Valid UpdateUserEmailRequest request) {
        service.selfUpdateEmail(request);
    }

    @GetMapping("/profile/{username}")
    public UserProfileResponse getProfile(@PathVariable String username) {
        User user = service.getByUsername(username);
        return mapper.toProfileResponse(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/ban")
    public void setBannedAt(@RequestBody @Valid UserBanRequest request) {
        service.setBannedAt(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/unban/{userId}")
    public void resetBannedAt(@PathVariable("userId") UUID userId) {
        service.resetBannedAt(userId);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody @Valid UpdateUserPasswordRequest request) {
        service.changePassword(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filter")
    public PageResponse<UserResponse> findToursByFilter(@RequestBody @Valid UserFilter filter) {
        var result = new PageResponse<UserResponse>();

        var users = service.findByFilter(filter);
        result.setTotalPages(users.getTotalPages());
        result.setTotalSize(users.getTotalElements());
        result.setPageNumber(users.getNumber());
        result.setPageSize(users.getSize());
        result.setContent(mapper.toResponse(users.getContent()));
        return result;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable UUID userId) {
        User user = service.getById(userId);
        return mapper.toResponse(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change-role")
    public void changeRole(@RequestBody @Valid UpdateUserRoleRequest request) {
        service.changeRole(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/is-superuser")
    public Boolean isSuperuser() {
        return service.isSuperuser();
    }

    @PostMapping("/delete")
    public void deleteUser(@RequestBody @Valid DeleteUserRequest request) {
        deleteService.deleteUser(request);
    }
}
