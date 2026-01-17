package com.congty9a4.backend.config.ggcloud;


import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("dev")
public class StorageConfig {

    @Bean
    public Storage storage() throws IOException {
        return StorageOptions.getDefaultInstance().getService();
    }
}
