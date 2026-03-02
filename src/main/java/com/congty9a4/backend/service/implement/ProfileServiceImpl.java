package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.annotation.TrackExecutionTime;
import com.congty9a4.backend.dto.req.ProfileCreationRequest;
import com.congty9a4.backend.dto.req.ProfileUpdateRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.ProfileResponse;
import com.congty9a4.backend.entity.Profile;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.ProfileMapper;
import com.congty9a4.backend.repository.jpa.ProfileRepository;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.service.storage.CloudStorageService;
import com.congty9a4.backend.service.ProfileService;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.PaginationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private PaginationHelper paginationHelper;
    @Autowired
    private CloudStorageService cloudStorageService;

    @Override
    public PageResponse<List<ProfileResponse>> getAllProfiles(AppPageable pageable) {
        log.info("Fetching all profiles with pagination");

        var currentPage = profileRepository.findAll(pageable.getPageable());

        return paginationHelper.buildPageResponse(
                currentPage,
                profileMapper::toProfileResponse,
                pageable
        );
    }


    @Override
    public ProfileResponse getProfileById(Integer id) {
        log.info("Fetching profile by ID: {}", id);

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.PROFILE_NOT_FOUND,
                        "Profile not found with id: " + id
                ));

        return profileMapper.toProfileResponse(profile);
    }

    @TrackExecutionTime
    @Override
    public ProfileResponse getProfileByUserId(UUID userId) {
        log.info("Fetching profile by user ID: {}", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.PROFILE_NOT_FOUND,
                        "Profile not found for user id: " + userId
                ));

        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public Profile getProfileEntityByUserId(UUID userId) {
        log.info("Fetching profile entity by user ID: {}", userId);

        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.PROFILE_NOT_FOUND,
                        "Profile not found for user id: " + userId
                ));
    }

    @Transactional
    @Override
    public ProfileResponse createProfile(UUID userId,
                                         ProfileCreationRequest request,
                                         MultipartFile avatar,
                                         MultipartFile coverBackground) {
        log.info("Creating profile for user ID: {}", userId);

        // Check if user exists
        Userchan user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        // Check if profile already exists for this user
        if (profileRepository.findByUserId(userId).isPresent()) {
            throw new AppException(
                    ErrorCode.PROFILE_ALREADY_EXISTS,
                    "Profile already exists for user id: " + userId
            );
        }

        // Check if fullname is already taken
        if (request.getFullname() != null &&
            profileRepository.existsByKeyName(request.getFullname())) {
            throw new AppException(
                    ErrorCode.PROFILE_FULLNAME_ALREADY_EXISTS,
                    "Full name already taken: " + request.getFullname()
            );
        }

        // Create profile
        Profile profile = profileMapper.toProfile(request);
        profile.setUser(user);
        profile.setAvatarUrl(saveMediaFile(avatar));
        profile.setCoverPhotoUrl(saveMediaFile(coverBackground));

        Profile savedProfile = profileRepository.save(profile);
        log.info("Profile created successfully with ID: {}", savedProfile.getId());

        return profileMapper.toProfileResponse(savedProfile);
    }

    @Transactional
    @Override
    public ProfileResponse updateProfile(Integer id, ProfileUpdateRequest request) {
        log.info("Updating profile with ID: {}", id);

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.PROFILE_NOT_FOUND,
                        "Profile not found with id: " + id
                ));

        // Check if fullname is being changed and if it's already taken
        if (request.getKeyName() != null &&
            !request.getKeyName().equals(profile.getKeyName()) &&
            profileRepository.existsByKeyName(request.getKeyName())) {
            throw new AppException(
                    ErrorCode.PROFILE_FULLNAME_ALREADY_EXISTS,
                    "Full name already taken: " + request.getKeyName()
            );
        }

        // Update profile fields
        profileMapper.update(profile, request);

        Profile updatedProfile = profileRepository.save(profile);
        log.info("Profile updated successfully with ID: {}", updatedProfile.getId());

        return profileMapper.toProfileResponse(updatedProfile);
    }

    @Transactional
    @Override
    public ProfileResponse updateProfileByUserId(UUID userId, ProfileUpdateRequest request) {
        log.info("Updating profile for user ID: {}", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.PROFILE_NOT_FOUND,
                        "Profile not found for user id: " + userId
                ));

        return updateProfile(profile.getId(), request);
    }

    @Transactional
    @Override
    public void deleteProfile(Integer id) {
        log.info("Deleting profile with ID: {}", id);

        if (!profileRepository.existsById(id)) {
            throw new AppException(
                    ErrorCode.PROFILE_NOT_FOUND,
                    "Profile not found with id: " + id
            );
        }

        profileRepository.deleteById(id);
        log.info("Profile deleted successfully with ID: {}", id);
    }

    @Transactional
    @Override
    public void deleteProfileByUserId(UUID userId) {
        log.info("Deleting profile for user ID: {}", userId);

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.PROFILE_NOT_FOUND,
                        "Profile not found for user id: " + userId
                ));

        profileRepository.delete(profile);
        log.info("Profile deleted successfully for user ID: {}", userId);
    }

    @Override
    public boolean existsForUser(UUID userId) {
        return profileRepository.findByUserId(userId).isPresent();
    }

    @Override
    public boolean isKeyNameTaken(String keyName) {
        return profileRepository.existsByKeyName(keyName);
    }

    private String saveMediaFile(MultipartFile file) {
        // Implement your file saving logic here

        if (file == null) return "";
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Cannot save empty file");
        }
        String url = cloudStorageService.uploadFile(file);
        log.info("FILE UPLOADED: " + url);
        return url;
    }
}

