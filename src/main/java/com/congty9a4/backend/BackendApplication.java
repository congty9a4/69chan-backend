package com.congty9a4.backend;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@Slf4j
// @SpringBootApplication
@SpringBootApplication(exclude = {
        com.google.cloud.spring.autoconfigure.storage.GcpStorageAutoConfiguration.class
})
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
