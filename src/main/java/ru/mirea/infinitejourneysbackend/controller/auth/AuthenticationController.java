package ru.mirea.infinitejourneysbackend.controller.auth;

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
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

}
