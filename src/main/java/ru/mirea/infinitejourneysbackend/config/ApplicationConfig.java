package ru.mirea.infinitejourneysbackend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final CloudStorageConfig cloudStorageConfig;

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
}
