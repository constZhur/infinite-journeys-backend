package ru.mirea.infinitejourneysbackend.unit.service.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.service.security.JwtServiceImpl;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    private Key key;

    @BeforeEach
    void setUp() throws Exception {
        Field jwtSigningKeyField = JwtServiceImpl.class.getDeclaredField("jwtSigningKey");
        String secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        jwtSigningKeyField.setAccessible(true);
        jwtSigningKeyField.set(jwtService, secretKey);
    }

    @Test
    void extractUserName_ValidToken_ReturnsUserName() {
        String token = generateToken("test");

        String userName = jwtService.extractUserName(token);

        assertEquals("test", userName);
    }

    @Test
    void generateToken_CustomUserDetails_ReturnsToken() {
        UserDetails userDetails = User.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void isTokenValid_ValidTokenAndUserDetails_ReturnsTrue() {
        String token = generateToken("test");
        UserDetails userDetails = User.builder()
                .username("test")
                .email("test@test.com")
                .password("password123")
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_TokenWithDifferentUserName_ReturnsFalse() {
        String token = generateToken("test");
        UserDetails userDetails = User.builder()
                .username("differentUser")
                .email("test@test.com")
                .password("password123")
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    private String generateToken(String subject) {
        return jwtService.generateToken(User.builder().username(subject).build());
    }

    private String generateExpiredToken(String subject) {
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key)
                .compact();
    }
}
