package com.congty9a4.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.congty9a4.backend.repository.jpa")
@EnableJpaAuditing
public class JpaConfig {
}
