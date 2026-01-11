package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.LoginRequest;
import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var result = authService.authenticate(req);
        return ApiResponse.success(result);
    }

    @GetMapping("/guest")
    public ApiResponse<AuthResponse> loginAsGuest() {
        return ApiResponse.success(authService.guest());
    }

}

