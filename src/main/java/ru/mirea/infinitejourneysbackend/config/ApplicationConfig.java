package ru.mirea.infinitejourneysbackend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final CloudStorageConfig cloudStorageConfig;
    private final CacheConfig cacheConfig;

    @Bean
    public AmazonS3 getClient() {
        AWSCredentials credentials = new BasicAWSCredentials(
                cloudStorageConfig.getUserId(),
                cloudStorageConfig.getAccessKey()
        );

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                cloudStorageConfig.getUrl(),
                                cloudStorageConfig.getRegion()
                        )
                )
                .build();
    }

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(cacheConfig.getInitialCapacity())
                .maximumSize(cacheConfig.getMaximumSize())
                .expireAfterAccess(cacheConfig.getExpireAfterAccess(),
                        TimeUnit.MINUTES));
        return cacheManager;
    }
}
