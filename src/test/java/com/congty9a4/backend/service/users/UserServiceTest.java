package com.congty9a4.backend.service.users;

import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.service.OtpService;
import com.congty9a4.backend.service.implement.UserServiceImpl;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper userMapper;

    @Mock
    OtpService otpService;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void createUser_withExistedEmail_andVerified_should_throwException() {
        // Given
        String email = "test@gmail.com";
        Userchan mockUser = Userchan.builder().email(email).isVerified(true).build();
        UserCreationRequest request = UserCreationRequest.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // When & Then
        AppException ex = assertThrows(AppException.class, () -> userService.createUser(request));
        assertEquals(ErrorCode.USER_ALREADY_EXISTS, ex.getErrorCode());
    }

    @Test
    void createUser_withExistedEmail_andUnverified_should_updateUser() {
        // Given
        String email = "unverified@gmail.com";
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";

        Userchan existingUser = Userchan.builder()
                .email(email)
                .username("oldUsername")
                .password("oldPassword")
                .isVerified(false)
                .build();

        UserCreationRequest request = UserCreationRequest.builder()
                .email(email)
                .username(newUsername)
                .password(newPassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(Userchan.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toUserResponse(any(Userchan.class))).thenReturn(new UserResponse());

        // When
        userService.createUser(request);

        // Then
        assertEquals(newUsername, existingUser.getUsername());
        assertEquals(encodedPassword, existingUser.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    void verifyEmailOtp_withInvalidOtp_should_throwException() {
        // Given
        String email = "test@gmail.com";
        String otp = "wrong-otp";

        when(otpService.validateOtp(email, otp)).thenReturn(false);

        // When & Then
        AppException ex = assertThrows(AppException.class, () -> userService.verifyEmailOtp(email, otp));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getErrorCode());
        assertEquals("OTP invalid or expired!", ex.getMessage());
    }

}
