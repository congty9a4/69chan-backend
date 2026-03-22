package com.congty9a4.backend.service;

import com.congty9a4.backend.config.security.JwtService;
import com.congty9a4.backend.constant.USER;
import com.congty9a4.backend.dto.req.auth.LoginRequest;
import com.congty9a4.backend.dto.req.auth.RefreshTokenRequest;
import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.congty9a4.backend.service.redis.RedisService;
import com.congty9a4.backend.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthService {

    final RedisService redisService;
    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final JwtService jwtService;
    final UserMapper userMapper;
    final UserRepository userRepository;
    final HttpServletRequest request;

    @NonFinal
    @Value("${google.client-id}")
    String googleClientId;

    public AuthResponse loginWithGoogle(Map<String, String> req) {
        String token = req.get("token");

        System.out.println(token);

        if (token == null || token.isBlank()) {
            throw new AppException(ErrorCode.GOOGLE_TOKEN_INVALID, "Google token not found in request!");
        }

        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId)).build();

            GoogleIdToken idToken = verifier.verify(token);

            String email = idToken.getPayload().getEmail();

            var user = userRepository.findByEmail(email).orElseGet(() -> {
                String baseUsername = email.length() > 50 ? email.substring(0, 50) : email;
                String encodedPassword = passwordEncoder.encode(java.util.UUID.randomUUID().toString());

                return userRepository.save(Userchan.builder()
                        .email(email)
                        .username(baseUsername)
                        .password(encodedPassword)
                        .isActive(true)
                        .isVerified(true)
                        .build());
            });

            return AuthResponse.builder()
                    .token(jwtService.createToken(user.getId().toString(), true))
                    .refreshToken(jwtService.createToken(user.getId().toString(), false))
                    // .user(userMapper.toInfochan(user)) // Mở comment nếu FE cần hiển thị thông
                    // tin
                    .build();

        } catch (IOException | java.security.GeneralSecurityException e) { // 1 in 2
            log.error("Google token verification failed", e);
            throw new AppException(ErrorCode.GOOGLE_TOKEN_INVALID, "Failed to verify Google token");
        }
    }

    public AuthResponse authenticate(LoginRequest req) {
        String email = req.getEmail();
        String requestedPassword = req.getPassword();

        Userchan user = userService.getUserByEmail(email);

        if (!user.isVerified()) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS,
                    "The account has not been email-verified! Please verify it before logging in.");
        }

        if (!passwordEncoder.matches(requestedPassword, user.getPassword()))
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "Wrong password");

        String token = jwtService.createToken(user.getId().toString(), true);
        String refreshToken = jwtService.createToken(user.getId().toString(), false);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userMapper.toInfochan(user))
                .build();
    }

    public AuthResponse guest() {
        Userchan guest = userService.getUserByEmail(USER.GUEST.EMAIL);
        String token = jwtService.createToken(guest.getId().toString(), true);
        String refreshToken = jwtService.createToken(guest.getId().toString(), false);
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userMapper.toInfochan(guest))
                .build();
    }

    // add blacklist mechanism later
    public AuthResponse refreshToken(String refreshToken) {
        jwtService.validateToken(refreshToken, false);
        return AuthResponse.builder()
                .token(jwtService.createToken(SecurityUtils.getCurrentUserId(), true))
                .build();
    }

    public void logout(RefreshTokenRequest req) {

        invalidateToken(req.getRefreshToken());

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            throw new AppException(ErrorCode.INVALID_TOKEN, "Token not found!");

        String accessToken = header.substring(7);

        invalidateToken(accessToken);

    }

    private void invalidateToken(String token) {
        long ttl = jwtService.extractTokenExpiration(token).getTime() - System.currentTimeMillis();

        if (ttl <= 0) {
            log.info("Token already expired, no need to blacklist");
            return;
        }

        String jid = jwtService.extractTokenId(token);
        log.info("Blacklisting token with JID {} for {} ms", jid, ttl);
        redisService.blacklistToken(jid, ttl);
    }

    public AuthResponse verifyAndLogin(String email, String otp) {
        userService.verifyEmailOtp(email, otp);

        Userchan verifiedUser = userService.getUserByEmail(email);

        String accessToken = jwtService.createToken(verifiedUser.getId().toString(), true);
        String refreshToken = jwtService.createToken(verifiedUser.getId().toString(), false);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toInfochan(verifiedUser))
                .build();

    }
}
