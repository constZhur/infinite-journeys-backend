package ru.mirea.infinitejourneysbackend.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.JwtAuthenticationResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignInRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignUpRequest;
import ru.mirea.infinitejourneysbackend.service.security.iterfaces.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации и авторизации",
        description = "Управление аутентификацией пользователей")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя",
            description = "Позволяет пользователю зарегистрироваться в системе.")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя",
            description = "Позволяет пользователю войти в систему.")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
