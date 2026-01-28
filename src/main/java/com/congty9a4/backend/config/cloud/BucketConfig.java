package com.congty9a4.backend.config.cloud;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "gcs")
@ConditionalOnProperty(name = "storage.provider", havingValue = "gcs", matchIfMissing = true)
public class BucketConfig {
    private String bucketName;
    private String subDirectory;
}
