package ru.mirea.infinitejourneysbackend.unit.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.JwtAuthenticationResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignInRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignUpRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Role;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.user.InvalidUserDataProblem;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;
import ru.mirea.infinitejourneysbackend.service.UserService;
import ru.mirea.infinitejourneysbackend.service.security.AuthenticationServiceImpl;
import ru.mirea.infinitejourneysbackend.service.security.iterfaces.JwtService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void signUp_Success() {
        SignUpRequest request = new SignUpRequest("username", "email", "password");
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password("encodedPassword")
                .balance(0.0)
                .role(Role.ROLE_BUYER)
                .build();

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userService.create(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.signUp(request);

        verify(userService).create(any(User.class));
        verify(jwtService).generateToken(any(User.class));
        assertThat(response.token()).isEqualTo("jwtToken");
    }

    @Test
    void signIn_Success() {
        SignInRequest request = new SignInRequest("username", "password");
        User user = User.builder().username(request.username()).build();
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user);
        assertThat(response.token()).isEqualTo("jwtToken");
    }

    @Test
    void signIn_Error_InvalidUserDataProblem() {
        SignInRequest request = new SignInRequest("nonExistingUsername", "password");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.signIn(request))
                .isInstanceOf(InvalidUserDataProblem.class);
    }
}
