package ru.mirea.infinitejourneysbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.infinitejourneysbackend.domain.dto.order.BuyOrderRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.order.OrderResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.user.*;
import ru.mirea.infinitejourneysbackend.domain.model.Order;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.mapper.OrderMapper;
import ru.mirea.infinitejourneysbackend.mapper.UserMapper;
import ru.mirea.infinitejourneysbackend.service.DeleteService;
import ru.mirea.infinitejourneysbackend.service.OrderService;
import ru.mirea.infinitejourneysbackend.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователей", description = "Управление пользователями")
@SecurityRequirement(name = "infinite-journeys-api")
public class UserController {
    private final UserService service;
    private final DeleteService deleteService;
    private final OrderService orderService;

    private final UserMapper mapper;
    private final OrderMapper orderMapper;

    @Operation(summary = "Изменение имени пользователя",
            description = "Позволяет пользователю изменить своё имя.")
    @PutMapping("/change-username")
    public void selfUpdateUsername(@RequestBody @Valid UpdateUserUsernameRequest request) {
        service.selfUpdateUsername(request);
    }

    @Operation(summary = "Изменение электронной почты пользователя",
            description = "Позволяет пользователю изменить свою электронную почту.")
    @PutMapping("/change-email")
    public void selfUpdateEmail(@RequestBody @Valid UpdateUserEmailRequest request) {
        service.selfUpdateEmail(request);
    }

    @Operation(summary = "Изменение пола пользователя",
            description = "Позволяет пользователю изменить свой пол.")
    @PutMapping("/change-gender")
    public void selfUpdateGender(@RequestBody @Valid UpdateUserGenderRequest request) {
        service.changeGender(request);
    }

    @Operation(summary = "Получение профиля пользователя",
            description = "Позволяет получить информацию о профиле пользователя.")
    @GetMapping("/profile/{username}")
    public UserProfileResponse getProfile(@PathVariable String username) {
        User user = service.getByUsername(username);
        return mapper.toProfileResponse(user);
    }

    @Operation(summary = "Блокировка пользователя",
            description = "Позволяет заблокировать пользователя.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/ban")
    public void setBannedAt(@RequestBody @Valid UserBanRequest request) {
        service.setBannedAt(request);
    }

    @Operation(summary = "Разблокировка пользователя",
            description = "Позволяет разблокировать заблокированного пользователя.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/unban/{userId}")
    public void resetBannedAt(@PathVariable("userId") UUID userId) {
        service.resetBannedAt(userId);
    }

    @Operation(summary = "Изменение пароля пользователя",
            description = "Позволяет пользователю изменить свой пароль.")
    @PostMapping("/change-password")
    public void changePassword(@RequestBody @Valid UpdateUserPasswordRequest request) {
        service.changePassword(request);
    }

    @Operation(summary = "Поиск пользователей по фильтру",
            description = "Позволяет найти пользователей по заданным критериям.")
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

    @Operation(summary = "Получение пользователя по ID",
            description = "Позволяет получить информацию о пользователе по его идентификатору.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable UUID userId) {
        User user = service.getById(userId);
        return mapper.toResponse(user);
    }

    @Operation(summary = "Изменение роли пользователя",
            description = "Позволяет изменить роль пользователя.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change-role")
    public void changeRole(@RequestBody @Valid UpdateUserRoleRequest request) {
        service.changeRole(request);
    }

    @Operation(summary = "Проверка на суперпользователя",
            description = "Позволяет проверить, является ли пользователь суперпользователем.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/is-superuser")
    public Boolean isSuperuser() {
        return service.isSuperuser();
    }

    @Operation(summary = "Удаление пользователя",
            description = "Позволяет удалить пользователя.")
    @PostMapping("/delete")
    public void deleteUser(@RequestBody @Valid DeleteUserRequest request) {
        deleteService.deleteUser(request);
    }

    @Operation(summary = "Получение текущего баланса пользователя",
            description = "Позволяет получить текущий баланс пользователя.")
    @GetMapping("/balance")
    public Double getCurrentBalance() {
        return service.getCurrentBalance();
    }

    @Operation(summary = "Пополнение баланса пользователя",
            description = "Позволяет пополнить баланс пользователя.")
    @PostMapping("/top-up-balance")
    public void topUpBalance(@RequestBody @Valid UpdateBalanceRequest request) {
        service.topUpBalance(request);
    }

    @Operation(summary = "Списание с баланса пользователя",
            description = "Позволяет списать сумму с баланса пользователя.")
    @PostMapping("/withdraw-from-balance")
    public void withdrawFromBalance(@RequestBody @Valid UpdateBalanceRequest request) {
        service.withdrawFromBalance(request);
    }

    @Operation(summary = "Получение собственных заказов",
            description = "Позволяет получить список собственных заказов.")
    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {
        List<Order> myOrders = orderService.getOrders();
        return orderMapper.toResponse(myOrders);
    }

    @Operation(summary = "Покупка заказа",
            description = "Позволяет пользователю совершить покупку.")
    @PostMapping("/buy-order")
    public OrderResponse buyOrder(@RequestBody @Valid BuyOrderRequest request) {
        Order order = orderService.createOrder(request);
        return orderMapper.toResponse(order);
    }
}

