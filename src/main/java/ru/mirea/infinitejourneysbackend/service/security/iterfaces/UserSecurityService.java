package ru.mirea.infinitejourneysbackend.service.security.iterfaces;


import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserSecurityService {
    UserDetailsService userDetailsService();
}
