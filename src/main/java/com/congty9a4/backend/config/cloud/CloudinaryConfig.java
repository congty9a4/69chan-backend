package com.congty9a4.backend.config.cloud;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ConditionalOnProperty(name = "storage.provider", havingValue = "cloudinary")
public class CloudinaryConfig {

    @Value("${cloudinary.cloud_name}")
    String CLOUD_NAME;

    @Value("${cloudinary.api_key}")
    String API_KEY;

    @Value("${cloudinary.api_secret}")
    String API_SECRET;

    @Bean
    public com.cloudinary.Cloudinary cloudinary() {
        return new com.cloudinary.Cloudinary(
                com.cloudinary.utils.ObjectUtils.asMap(
                        "cloud_name", CLOUD_NAME,
                        "api_key", API_KEY,
                        "api_secret", API_SECRET));
    }
}
