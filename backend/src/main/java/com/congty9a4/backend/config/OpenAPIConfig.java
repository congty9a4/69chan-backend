package com.congty9a4.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Info info = new Info()
                .title("My API")
                .version("1.0")
                .description("This is a sample API.");

        return new OpenAPI().info(info);
    }
}

