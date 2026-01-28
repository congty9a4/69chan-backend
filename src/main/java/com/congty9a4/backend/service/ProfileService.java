package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.ProfileCreationRequest;
import com.congty9a4.backend.dto.req.ProfileUpdateRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.ProfileResponse;
import com.congty9a4.backend.entity.Profile;
import com.congty9a4.backend.util.AppPageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProfileService {

    /**
     * Get all profiles with pagination
     */
    PageResponse<List<ProfileResponse>> getAllProfiles(AppPageable pageable);

    /**
     * Get profile by ID
     */
    ProfileResponse getProfileById(Integer id);

    /**
     * Get profile by user ID
     */
    ProfileResponse getProfileByUserId(UUID userId);

    /**
     * Get profile entity by user ID (for internal use)
     */
    Profile getProfileEntityByUserId(UUID userId);

    /**
     * Create a new profile for a user
     */
    ProfileResponse createProfile(UUID userId, ProfileCreationRequest request, MultipartFile avatar,MultipartFile coverImage);

    /**
     * Update an existing profile
     */
    ProfileResponse updateProfile(Integer id, ProfileUpdateRequest request);

    /**
     * Update profile by user ID
     */
    ProfileResponse updateProfileByUserId(UUID userId, ProfileUpdateRequest request);

    /**
     * Delete a profile by ID
     */
    void deleteProfile(Integer id);

    /**
     * Delete profile by user ID
     */
    void deleteProfileByUserId(UUID userId);

    /**
     * Check if a profile exists for a user
     */
    boolean existsForUser(UUID userId);

    /**
     * Check if keyName is already taken
     */
    boolean isKeyNameTaken(String keyName);
}

