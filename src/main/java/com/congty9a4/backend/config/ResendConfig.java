package com.congty9a4.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "resend.api")
@Data
public class ResendConfig {

    private String key;
    private String url;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
