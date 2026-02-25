package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.LoginRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and authorization APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with credentials and return JWT token")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var result = authService.authenticate(req);
        return ApiResponse.success(result);
    }

    @GetMapping("/guest")
    @Operation(summary = "Guest login", description = "Login as a guest user without credentials")
    public ApiResponse<AuthResponse> loginAsGuest() {
        return ApiResponse.success(authService.guest());
    }

    @GetMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Refresh JWT token using a valid refresh token")
    public ApiResponse<AuthResponse> refreshToken(@RequestParam("token") String refreshToken) {
        var result = authService.refreshToken(refreshToken);
        return ApiResponse.success(result);
    }

    @PostMapping("/google")
    @Operation(summary = "Google Login", description = "Login or Register using Google ID Token")
    public ApiResponse<AuthResponse> loginGoogle(@RequestBody LoginRequest req) {
        var result = authService.loginWithGoogle(req.token);
        return ApiResponse.success(result);
    }
}
