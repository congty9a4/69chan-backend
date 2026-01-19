package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sample")
@Tag(name = "Sample", description = "Sample and testing endpoints for development purposes")
public class SampleController {

    private final UserMapper userMapper;

    public SampleController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/endpoint")
    @Operation(summary = "Sample endpoint", description = "Returns a simple sample message for testing")
    public Map<String, String> getSample() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a sample endpoint");
        return response;
    }

    @GetMapping("/error")
    @Operation(summary = "Sample error", description = "Throws a sample runtime exception for testing error handling")
    public void getError() {
        throw new RuntimeException("This is a sample error");
    }

    @GetMapping("/user")
    @Operation(summary = "Sample user", description = "Returns a sample user object for testing purposes")
    public ApiResponse<UserResponse> getSampleUser() {
        return ApiResponse.success(
                userMapper.toUserResponse(
                        Userchan.builder()
                                .id(UUID.randomUUID())
                                .email("testEmail")
                                .username("testUser")
                                .build()
                )
        );
    }
}

