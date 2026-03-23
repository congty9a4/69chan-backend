package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.annotation.TrackExecutionTime;
import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.service.EmailService;
import com.congty9a4.backend.service.OtpService;
import com.congty9a4.backend.service.RelationService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.PaginationHelper;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final EmailService emailService;
    private final OtpService otpService;

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    UserMapper userMapper;

    PaginationHelper paginationHelper;

    RelationService relationService;

    @Override
    public PageResponse<List<UserResponse>> getAllUsers(AppPageable pageable) {
        var currentPage = userRepository.findAll(pageable.getPageable());

        return paginationHelper.buildPageResponse(
                currentPage,
                userMapper::toUserResponse,
                pageable);
    }

    @TrackExecutionTime
    @Override
    public Userchan getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest userReq) {
        if (userRepository.existsByEmail(userReq.getEmail()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS,
                    "User already exists with email: " + userReq.getEmail());

        Userchan user = Userchan.builder()
                .username(userReq.getUsername())
                .password(userReq.getPassword())
                .email(userReq.getEmail())
                .isVerified(false)
                .build();

        String rawPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
        Userchan savedUser = userRepository.save(user);
        String otp = otpService.generateAndSaveOtp(savedUser.getEmail());
        emailService.sendOtpEmail(savedUser.getEmail(), otp);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public Userchan updateUser(UUID id, UserUpdationRequest user) {
        return userRepository.findById(id).map(existingUser -> {
            // if exists, update fields
            userMapper.update(existingUser, user);
            // return for existingUser
            return userRepository.save(existingUser);

            // if not found, throw exception
        }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));

    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Userchan getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found with email: " + email));
    }

    @Override
    public Infochan userInfo(String userId) {
        Userchan user = userRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND, "User of this post not found with id: " + userId));
        return userMapper.toInfochan(user);
    }

    @Override
    @Transactional
    public void handleFollow(String targetUserId) {
        getUserById(UUID.fromString(targetUserId)); // check if user exists
        relationService.follow(SecurityUtils.getCurrentUserId(), targetUserId);
    }

    @Override
    @Transactional
    public void handleUnfollow(String targetUserId) {
        getUserById(UUID.fromString(targetUserId)); // check if user exists
        relationService.unfollow(SecurityUtils.getCurrentUserId(), targetUserId);
    }

    @Override
    @Transactional
    public void verifyEmailOtp(String email, String otp) {

        boolean isValid = otpService.validateOtp(email, otp);
        if (!isValid) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "OTP invalid or expired!");
        }

        Userchan user = getUserByEmail(email);
        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public void resendVerificationOtp(String email) {

        Userchan user = getUserByEmail(email);

        if (user.isVerified()) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "This account has already been verified!");
        }

        String newOtp = otpService.generateAndSaveOtp(email);
        emailService.sendOtpEmail(email, newOtp);
    }

}
