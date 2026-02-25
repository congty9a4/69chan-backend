package com.congty9a4.backend.config.cloud;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "storage.provider", havingValue = "gcs", matchIfMissing = true)
public class StorageConfig {

    @ConditionalOnProperty(name = "storage.provider", havingValue = "gcs")

    @Bean
    public Storage storage() throws IOException {
        return StorageOptions.getDefaultInstance().getService();
    }
}
