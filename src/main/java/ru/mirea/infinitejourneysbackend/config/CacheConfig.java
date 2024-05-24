package ru.mirea.infinitejourneysbackend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Getter
public class CacheConfig {

    @Value("${cache.initial-capacity}")
    private int initialCapacity;

    @Value("${cache.maximum-size}")
    private int maximumSize;

    @Value("${cache.expire-after-access}")
    private int expireAfterAccess;
}
