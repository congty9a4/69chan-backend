package com.congty9a4.backend.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "gcs")
public class BucketConfig {
    private String bucketName;
    private String subDirectory;
}
