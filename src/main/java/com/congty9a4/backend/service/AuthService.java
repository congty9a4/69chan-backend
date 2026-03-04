package com.congty9a4.backend.service;

import com.congty9a4.backend.config.security.JwtService;
import com.congty9a4.backend.constant.USER;
import com.congty9a4.backend.dto.req.LoginRequest;
import com.congty9a4.backend.dto.req.RefreshTokenRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;

import java.util.Collections;

import com.congty9a4.backend.service.redis.RedisService;
import com.congty9a4.backend.util.SecurityUtils;
import jakarta.persistence.FieldResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RedisService redisService;
    PasswordEncoder passwordEncoder;

    UserService userService;

    JwtService jwtService;

    UserMapper userMapper;

    UserRepository userRepository;

    HttpServletRequest request;

    @Value("${google.client-id}")
    String googleClientId;

    public AuthResponse loginWithGoogle(String googleToken)
            throws java.security.GeneralSecurityException, java.io.IOException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId)).build();

        GoogleIdToken idToken = verifier.verify(googleToken);

        if (idToken == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String email = idToken.getPayload().getEmail();

        var user = userRepository.findByEmail(email).orElseGet(() -> {
            String baseUsername = email.length() > 50 ? email.substring(0, 50) : email;
            String encodedPassword = passwordEncoder.encode(java.util.UUID.randomUUID().toString());

            return userRepository.save(Userchan.builder()
                    .email(email)
                    .username(baseUsername)
                    .password(encodedPassword)
                    .isActive(true)
                    .build());
        });

        return AuthResponse.builder()
                .token(jwtService.createToken(user.getId().toString(), true))
                .refreshToken(jwtService.createToken(user.getId().toString(), false))
                // .user(userMapper.toInfochan(user)) // Mở comment nếu FE cần hiển thị thông
                // tin
                .build();
    }

    public AuthResponse authenticate(LoginRequest req) {
        String email = req.getEmail();
        String requestedPassword = req.getPassword();

        Userchan user = userService.getUserByEmail(email);

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

        String accessToken =  header.substring(7);

        invalidateToken(accessToken);

    }

    private void invalidateToken(String token) {
        long ttl = jwtService.extractTokenExpiration(token).getTime() - System.currentTimeMillis();

        if (ttl <= 0){
            log.info("Token already expired, no need to blacklist");
            return;
        }

        String jid = jwtService.extractTokenId(token);
        log.info("Blacklisting token with JID {} for {} ms", jid, ttl);
        redisService.blacklistToken(jid, ttl);
    }

}
