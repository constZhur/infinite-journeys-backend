package ru.mirea.infinitejourneysbackend.config.security;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Configuration
@Getter
@RequiredArgsConstructor
public class SuperUserConfig {

    @Value("${superuser.password}")
    private String superuserDefaultPassword;

    @Value("${superuser.id}")
    private UUID superuserId;

    @Value("${superuser.enabled}")
    private boolean superuserEnabled = false;

    public boolean isSuperuser(UUID userId) {
        return userId.equals(superuserId) && superuserEnabled;
    }
}
