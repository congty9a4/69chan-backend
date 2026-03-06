package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.auth.LoginRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_whenSuccess_thenReturnToken() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("test-token");

        when(authService.authenticate(any(LoginRequest.class))).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("test-token"));
    }

    @Test
    void loginAsGuest_whenSuccess_thenReturnToken() throws Exception {
        // Given
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("guest-token");

        when(authService.guest()).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(get("/api/auth/guest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("guest-token"));
    }
}

