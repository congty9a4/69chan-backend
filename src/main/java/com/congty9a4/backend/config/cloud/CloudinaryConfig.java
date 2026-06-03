package com.congty9a4.backend.config.cloud;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ConditionalOnProperty(name = "storage.provider", havingValue = "cloudinary", matchIfMissing = true)
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    String cloudName;

    @Value("${cloudinary.api-key}")
    String apiKey;

    @Value("${cloudinary.api-secret}")
    String apiSecret;

    @Bean
    public com.cloudinary.Cloudinary cloudinary() {
        return new com.cloudinary.Cloudinary(
                com.cloudinary.utils.ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret));
    }
}
