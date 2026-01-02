package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.dto.resp.user.UserResponse;
import com.congty9a4.backend.entity.relational.Userchan;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.AppPageable;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users API")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ApiResponse<PageResponse<List<UserResponse>>> getAllUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "id") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        HttpServletRequest request
    ) {

        var results = userService.getAllUsers(AppPageable.of(page, size, sortBy, sortDir, request.getServletPath()));

        return ApiResponse.success(
                results
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable UUID id) {
        var userchan = userService.getUserById(id);
        return ApiResponse.success(
                userMapper.toUserResponse(userchan)
        );
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userReq) {
        var userchan = userService.createUser(userReq);
        return ApiResponse.success(
                userMapper.toUserResponse(userchan)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserUpdationRequest user) {
        var userchan = userService.updateUser(id, user);
        return ApiResponse.success(
                userMapper.toUserResponse(userchan)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.success(
                String.format("User with id=%s is deleted successfully", id.toString())
        );
    }
}

