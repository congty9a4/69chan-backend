package com.congty9a4.backend.controller;

import com.congty9a4.backend.constant.CustomLocale;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.dto.resp.user.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    private final UserMapper userMapper;

    public SampleController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/endpoint")
    public Map<String, String> getSample() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a sample endpoint");
        return response;
    }

    @GetMapping("/error")
    public void getError() {
        throw new RuntimeException("This is a sample error");
    }

    @GetMapping("/user")
    public ApiResponse<UserResponse> getSampleUser() {
        return ApiResponse.success(
                "Sample user data",
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

