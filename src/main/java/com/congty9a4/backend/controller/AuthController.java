package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.auth.LoginRequest;
import com.congty9a4.backend.dto.req.auth.RefreshTokenRequest;
import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.AuthService;
import com.congty9a4.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Authentication and authorization APIs")
public class AuthController {

    private AuthService authService;

    private UserService userService;


    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with credentials and return JWT token")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var result = authService.authenticate(req);
        return ApiResponse.success(result);
    }


    @PostMapping("/register")
    @Operation(summary = "Create user", description = "Create a new user account")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userReq) {
        return ApiResponse.success(
                userService.createUser(userReq)
        );
    }

    @GetMapping("/guest")
    @Operation(summary = "Guest login", description = "Login as a guest user without credentials")
    public ApiResponse<AuthResponse> loginAsGuest() {
        return ApiResponse.success(authService.guest());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Refresh JWT token using a valid refresh token")
    public ApiResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest req) {
        var result = authService.refreshToken(req.getRefreshToken());
        return ApiResponse.success(result);
    }

    @PostMapping("/login/google")
    @Operation(summary = "Google Login", description = "Login or Register using Google ID Token")
    public ApiResponse<AuthResponse> loginGoogle(@RequestBody java.util.Map<String, String> req){
        return ApiResponse.success(authService.loginWithGoogle(req));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidate the current user's token")
    public ApiResponse<Void> logout(@RequestBody RefreshTokenRequest req) {
        authService.logout(req);
        return ApiResponse.success(null);
    }
}
