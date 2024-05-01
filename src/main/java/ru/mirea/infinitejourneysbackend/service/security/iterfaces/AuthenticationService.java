package ru.mirea.infinitejourneysbackend.service.security.iterfaces;

import ru.mirea.infinitejourneysbackend.domain.dto.auth.JwtAuthenticationResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignInRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
