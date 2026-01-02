package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.user.UserResponse;
import com.congty9a4.backend.entity.relational.Userchan;
import com.congty9a4.backend.util.AppPageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    PageResponse<List<UserResponse>> getAllUsers(AppPageable pageable);
    Userchan getUserById(UUID id);
    Userchan createUser(UserCreationRequest user);
    Userchan updateUser(UUID id, UserUpdationRequest user);
    void deleteUser(UUID id);
}

