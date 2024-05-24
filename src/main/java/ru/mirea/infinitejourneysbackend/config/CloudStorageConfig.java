package ru.mirea.infinitejourneysbackend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Getter
@Component
@RequiredArgsConstructor
public class CloudStorageConfig {
    @Value("${storage.cloud.user-id}")
    private String userId;

    @Value("${storage.cloud.access-key}")
    private String accessKey;

    @Value("${storage.cloud.bucket-name}")
    private String bucketName;

    @Value("${storage.cloud.url}")
    private String url;

    @Value("${storage.cloud.region}")
    private String region;
}
