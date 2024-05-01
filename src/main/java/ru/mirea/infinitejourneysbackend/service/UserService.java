package ru.mirea.infinitejourneysbackend.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mirea.infinitejourneysbackend.config.security.SuperUserConfig;
import ru.mirea.infinitejourneysbackend.domain.dto.user.*;
import ru.mirea.infinitejourneysbackend.domain.model.Role;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.user.*;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final SuperUserConfig superUserConfig;

    @PostConstruct
    public void initSuperUser() {
        if (!superUserConfig.isSuperuserEnabled()) {
            return;
        }
        var superuserId = superUserConfig.getSuperuserId();
        var user = repository.findById(superuserId);

        if (user.isEmpty()) {
            User u = new User();
            u.setId(superuserId);
            u.setUsername("superuser");
            u.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com");
            u.setPassword(passwordEncoder.encode(superUserConfig.getSuperuserDefaultPassword()));
            u.setRole(Role.ROLE_ADMIN);
            repository.save(u);
            log.info("Создан суперпользователь с именем пользователя superuser и паролем superuser");
        } else {
            if (!user.get().isAdmin()) {
                user.get().setRole(Role.ROLE_ADMIN);
                repository.save(user.get());
                log.info("Пользователь с ID {} теперь является суперпользователем", superuserId);
            }
        }
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserNotUniqueUsernameProblem(user.getUsername());
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new UserNotUniqueEmailProblem(user.getEmail());
        }

        return save(user);
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public Optional<User> findById(UUID userId) {
        return repository.findById(userId);
    }

    public User getById(UUID userId) {
        return findById(userId).orElseThrow(UserNotFoundProblem::new);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User getByUsername(String username) {
        var user = findByUsername(username)
                .orElseThrow(UserNotFoundProblem::new);
        if (user.isEnabled()) {
            return user;
        } else {
            throw new UserDeletedProblem();
        }
    }

    public void setBannedAt(UserBanRequest request) {
        User currentUser = getCurrentUser();
        User user = getById(request.id());

        if (!hasAccessToUser(currentUser, user)) {
            throw new ForbiddenAccessProblem();
        }

        user.setBannedAt(OffsetDateTime.now()
                .plusDays(Optional.ofNullable(request.days()).orElse(0))
                .plusHours(Optional.ofNullable(request.hours()).orElse(0))
                .plusMinutes(Optional.ofNullable(request.minutes()).orElse(0)));

        save(user);
    }

    public void resetBannedAt(UUID userId) {
        User currentUser = getCurrentUser();
        User user = getById(userId);

        if (!hasAccessToUser(currentUser, user)) {
            throw new ForbiddenAccessProblem();
        }

        user.setBannedAt(null);
        save(user);
    }

    public void changePassword(UpdateUserPasswordRequest request) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(request.oldPassword(), currentUser.getPassword())) {
            throw new InvalidUserPasswordProblem();
        }

        currentUser.setPassword(passwordEncoder.encode(request.newPassword()));
        save(currentUser);
    }

    public void changeRole(UpdateUserRoleRequest request) {
        User currentUser = getCurrentUser();
        User user = getById(request.id());

        if (!hasAccessToUser(currentUser, user)) {
            throw new ForbiddenAccessProblem();
        }

        user.setRole(Role.valueOf(request.role()));
        save(user);
    }


    public void selfUpdateUsername(UpdateUserUsernameRequest request) {
        User current = getCurrentUser();
        String username = request.username();

        if (repository.existsByUsername(username)
                && !current.getUsername().equals(username)) {
            throw new UserNotUniqueUsernameProblem(username);
        }

        current.setUsername(username);
        save(current);
    }

    public void selfUpdateEmail(UpdateUserEmailRequest request) {
        User user = getCurrentUser();
        String email = request.email();

        if (repository.existsByEmail(email)
                && !user.getEmail().equals(email)) {
            throw new UserNotUniqueEmailProblem(email);
        }

        user.setEmail(email);
        save(user);
    }

    public Page<User> findByFilter(UserFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        return repository.findAllWithFilter(
                filter.getId(),
                filter.getUsername(),
                filter.getEmail(),
                Optional.ofNullable(filter.getRole()).map(Role::valueOf).orElse(null),
                filter.getIsBanned(),
                OffsetDateTime.now(),
                pageable
        );
    }

    public boolean isSuperuser() {
        var user = getCurrentUser();
        return superUserConfig.isSuperuser(user.getId());
    }

    public boolean hasAccessToUser(User currentUser, User user) {
        /*
            Права доступа:
            Суперпользователь - Админ - да
            Суперпользователь - Модератор - да
            Суперпользователь - Пользователь - да

            Админ - Админ - нет
            Админ - Продавец - да
            Админ - Покупатель - да

            Продавец - Продавец - нет
            Продавец - Покупатель - нет

            Покупатель - Покупатель - нет
            Покупатель - Продавец - нет

            Управлять собой - нет
         */

        if (currentUser.getId().equals(user.getId())) {
            return false;
        }

        if (superUserConfig.isSuperuser(currentUser.getId())) {
            return true;
        }

        // Проверка никогда не должна сработать, но предосторожность не помешает
        if (superUserConfig.isSuperuser(user.getId())) {
            return false;
        }

        if (currentUser.isAdmin()) {
            return !user.getRole().equals(Role.ROLE_ADMIN);
        }

        // Ни покупатель, ни продавец не имеют доступа к другим пользователям
        return false;
    }


}
