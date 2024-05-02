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
    public String userId;

    @Value("${storage.cloud.access-key}")
    public String accessKey;

    @Value("${storage.cloud.bucket-name}")
    public String bucketName;

    @Value("${storage.cloud.url}")
    public String url;

    @Value("${storage.cloud.region}")
    public String region;

}
