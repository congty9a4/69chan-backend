package com.congty9a4.backend.integration;

import com.congty9a4.backend.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {
    @Autowired
    private UserController userController;

    @Test
    void contextLoads(){
        // Test will fail if the application context cannot start
        assertThat(userController).isNotNull();
    }
}
