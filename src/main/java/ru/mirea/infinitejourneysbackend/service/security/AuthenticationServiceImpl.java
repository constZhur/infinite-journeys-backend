package ru.mirea.infinitejourneysbackend.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.JwtAuthenticationResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignInRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignUpRequest;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.domain.model.Role;
import ru.mirea.infinitejourneysbackend.exception.user.InvalidUserDataProblem;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;
import ru.mirea.infinitejourneysbackend.service.UserService;
import ru.mirea.infinitejourneysbackend.service.security.iterfaces.AuthenticationService;
import ru.mirea.infinitejourneysbackend.service.security.iterfaces.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_BUYER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);

    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        var user = userRepository
                .findByUsername(request.username())
                .orElseThrow(InvalidUserDataProblem::new);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);

    }
}
