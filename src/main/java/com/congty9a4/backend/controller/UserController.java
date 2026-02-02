package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.service.RelationService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.AppPageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {

    UserService userService;

    UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
    public ApiResponse<PageResponse<List<UserResponse>>> getAllUsers(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "id") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir

    ) {

        var results = userService.getAllUsers(AppPageable.of(page, size, sortBy, sortDir));

        return ApiResponse.success(
                results
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their unique identifier")
    public ApiResponse<UserResponse> getUserById(@PathVariable UUID id) {
        var userchan = userService.getUserById(id);
        return ApiResponse.success(
                userMapper.toUserResponse(userchan)
        );
    }

    @PostMapping("/create")
    @Operation(summary = "Create user", description = "Create a new user account")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userReq) {
        var userchan = userService.createUser(userReq);
        return ApiResponse.success(
                userMapper.toUserResponse(userchan)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user's information")
    public ApiResponse<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserUpdationRequest user) {
        var userchan = userService.updateUser(id, user);
        return ApiResponse.success(
                userMapper.toUserResponse(userchan)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user account by ID")
    public ApiResponse<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.success(
                String.format("User with id=%s is deleted successfully", id.toString())
        );
    }

    @PostMapping("/follow/{id}")
    @Operation(summary = "Follow/unfollow user", description = "Toggle follow status for a user")
    public void handleFollow(@PathVariable String id) {
        userService.handleFollow(id);
    }

    @DeleteMapping("/unfollow/{id}")
    @Operation(summary = "Unfollow user", description = "Unfollow a user by ID")
    public void handleUnfollow(@PathVariable String id) {
        userService.handleUnfollow(id);
    }
}

