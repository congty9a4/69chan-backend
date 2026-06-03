package com.congty9a4.backend.service.auth;

import com.congty9a4.backend.config.security.JwtService;
import com.congty9a4.backend.dto.req.auth.LoginRequest;
import com.congty9a4.backend.dto.req.auth.RefreshTokenRequest;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.service.AuthService;
import com.congty9a4.backend.service.EmailService;
import com.congty9a4.backend.service.OtpService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.service.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
class AuthServiceTest {

    @Mock
    UserService userService;

    @Mock
    JwtService jwtService;

    @Mock
    RedisService redisService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    HttpServletRequest request;

    @Mock
    EmailService emailService;

    @Mock
    OtpService otpService;

    @InjectMocks
    AuthService authService;

    @Test
    void loginWithWrongPassword_should_throwException() {
        // Given
        String email = "test@gmail";
        String expectedPassword = "password";
        String wrongPassword = "wrongpassword";

        Userchan user = Userchan.builder()
                .email(email)
                .password(expectedPassword)
                .build();

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(wrongPassword, expectedPassword)).thenReturn(false);

        LoginRequest loginRequest = LoginRequest.builder().email(email).password(wrongPassword).build();

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> authService.authenticate(loginRequest));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
    }

    @Test
    void loginWithUnverifiedAccount_should_generateOtpAndThrowException() {
        // Given
        String email = "unverified@gmail.com";
        String password = "password";
        String otp = "123456";

        Userchan user = Userchan.builder()
                .email(email)
                .password(password)
                .isVerified(false)
                .build();

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, password)).thenReturn(true);
        when(otpService.generateAndSaveOtp(email)).thenReturn(otp);

        LoginRequest loginRequest = LoginRequest.builder().email(email).password(password).build();


        AppException exception = assertThrows(AppException.class, () -> authService.authenticate(loginRequest));
        assertEquals(ErrorCode.USER_NOT_VERIFIED, exception.getErrorCode());

        verify(otpService).generateAndSaveOtp(email);
        verify(emailService).sendOtpEmail(email, otp);
    }

    @Test
    void loginWithGoogle_missingToken_should_throwException() {
        // Given
        Map<String, String> req = new HashMap<>();

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> authService.loginWithGoogle(req));
        assertEquals(ErrorCode.GOOGLE_TOKEN_INVALID, exception.getErrorCode());
        assertEquals("Google token not found in request!", exception.getMessage());
    }

    @Test
    void loginWithGoogle_invalidToken_should_throwException() {
        // Given
        Map<String, String> req = new HashMap<>();
        req.put("token", "invalid-google-token");

        // When & Then
        // The GoogleIdTokenVerifier.verify() throws IllegalArgumentException for malformed tokens
        assertThrows(IllegalArgumentException.class, () -> authService.loginWithGoogle(req));
    }

    @Test
    void logout_should_blacklistBothTokens() {
        // Given
        String accessToken = "Bearer access-token";
        String rawAccessToken = "access-token";
        String refreshToken = "refresh-token";
        RefreshTokenRequest logoutReq = RefreshTokenRequest.builder().refreshToken(refreshToken).build();

        when(request.getHeader("Authorization")).thenReturn(accessToken);

        // Mocking for refresh token
        when(jwtService.extractTokenExpiration(refreshToken)).thenReturn(new Date(System.currentTimeMillis() + 10000));
        when(jwtService.extractTokenId(refreshToken)).thenReturn("refresh-jid");

        // Mocking for access token
        when(jwtService.extractTokenExpiration(rawAccessToken)).thenReturn(new Date(System.currentTimeMillis() + 5000));
        when(jwtService.extractTokenId(rawAccessToken)).thenReturn("access-jid");

        // When
        authService.logout(logoutReq);

        // Then
        verify(redisService).blacklistToken(eq("refresh-jid"), anyLong());
        verify(redisService).blacklistToken(eq("access-jid"), anyLong());
    }

    @Test
    void refreshToken_withBlacklistedToken_should_throwException() {
        // Given
        String refreshToken = "blacklisted-refresh-token";

        doThrow(new AppException(ErrorCode.INVALID_TOKEN, "Token has been revoked"))
                .when(jwtService).validateToken(refreshToken, false);


        AppException exception = assertThrows(AppException.class, () -> authService.refreshToken(refreshToken));
        assertEquals(ErrorCode.INVALID_TOKEN, exception.getErrorCode());
        assertEquals("Token has been revoked", exception.getMessage());
    }
}
