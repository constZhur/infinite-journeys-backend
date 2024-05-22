package ru.mirea.infinitejourneysbackend.unit.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;
import ru.mirea.infinitejourneysbackend.service.security.UserSecurityServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSecurityServiceImpl userSecurityService;

    @Test
    public void testUserDetailsService_ExistingUser() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userSecurityService.userDetailsService().loadUserByUsername("testuser");

        assertThat("testuser").isEqualTo(userDetails.getUsername());

        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testUserDetailsService_NonExistingUser() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userSecurityService.userDetailsService().loadUserByUsername("unknownuser");
        });

        verify(userRepository, times(1)).findByUsername("unknownuser");
    }
}
