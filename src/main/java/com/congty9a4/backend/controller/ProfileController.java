package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.ProfileCreationRequest;
import com.congty9a4.backend.dto.req.ProfileUpdateRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.ProfileResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.ProfileService;
import com.congty9a4.backend.util.AppPageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profile", description = "Profile management APIs")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get all profiles", description = "Get all profiles with pagination")
    public ApiResponse<PageResponse<List<ProfileResponse>>> getAllProfiles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            HttpServletRequest request
    ) {
        log.info("GET /api/v1/profiles - page: {}, size: {}, sortBy: {}, sortDir: {}",
                 page, size, sortBy, sortDir);

        AppPageable pageable = AppPageable.of(page, size, sortBy, sortDir);
        PageResponse<List<ProfileResponse>> profiles = profileService.getAllProfiles(pageable);

        return ApiResponse.success(profiles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get profile by ID", description = "Get a profile by its ID")
    public ApiResponse<ProfileResponse> getProfileById(@PathVariable Integer id) {
        log.info("GET /api/v1/profiles/{}", id);

        ProfileResponse profile = profileService.getProfileById(id);
        return ApiResponse.success(profile);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get profile by user ID", description = "Get a profile by user ID")
    public ApiResponse<ProfileResponse> getProfileByUserId(@PathVariable UUID userId) {
        log.info("GET /api/v1/profiles/user/{}", userId);

        ProfileResponse profile = profileService.getProfileByUserId(userId);
        return ApiResponse.success(profile);
    }

    @PostMapping(value = "/user/{userId}", consumes = "multipart/form-data")
    @Operation(summary = "Create profile", description = "Create a new profile for a user")
    public ApiResponse<ProfileResponse> createProfile(
            @PathVariable UUID userId,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart(value = "coverBackground", required = false) MultipartFile coverBackground,
            @Valid @RequestPart("profile") ProfileCreationRequest request
    ) {
        log.info("POST /api/v1/profiles/user/{} - Creating profile", userId);

        ProfileResponse profile = profileService.createProfile(userId, request, avatar, coverBackground);
        return ApiResponse.success(profile);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update profile", description = "Update an existing profile by ID")
    public ApiResponse<ProfileResponse> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        log.info("PUT /api/v1/profiles/{} - Updating profile", id);

        ProfileResponse profile = profileService.updateProfile(id, request);
        return ApiResponse.success(profile);
    }

    @PutMapping("/user/{userId}")
    @Operation(summary = "Update profile by user ID", description = "Update a profile by user ID")
    public ApiResponse<ProfileResponse> updateProfileByUserId(
            @PathVariable UUID userId,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        log.info("PUT /api/v1/profiles/user/{} - Updating profile", userId);

        ProfileResponse profile = profileService.updateProfileByUserId(userId, request);
        return ApiResponse.success(profile);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete profile", description = "Delete a profile by ID")
    public ApiResponse<Void> deleteProfile(@PathVariable Integer id) {
        log.info("DELETE /api/v1/profiles/{} - Deleting profile", id);

        profileService.deleteProfile(id);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "Delete profile by user ID", description = "Delete a profile by user ID")
    public ApiResponse<Void> deleteProfileByUserId(@PathVariable UUID userId) {
        log.info("DELETE /api/v1/profiles/user/{} - Deleting profile", userId);

        profileService.deleteProfileByUserId(userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/user/{userId}/exists")
    @Operation(summary = "Check if profile exists", description = "Check if a profile exists for a user")
    public ApiResponse<Boolean> existsForUser(@PathVariable UUID userId) {
        log.info("GET /api/v1/profiles/user/{}/exists - Checking if profile exists", userId);

        boolean exists = profileService.existsForUser(userId);
        return ApiResponse.success(exists);
    }

    @GetMapping("/check-fullname")
    @Operation(summary = "Check if fullname is taken", description = "Check if a fullname is already taken")
    public ApiResponse<Boolean> isFullNameTaken(@RequestParam String fullName) {
        log.info("GET /api/v1/profiles/check-fullname?fullName={}", fullName);

        boolean taken = profileService.isFullNameTaken(fullName);
        return ApiResponse.success(taken);
    }
}

