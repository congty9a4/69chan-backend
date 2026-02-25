package com.congty9a4.backend.service;

import com.congty9a4.backend.config.security.JwtService;
import com.congty9a4.backend.constant.USER;
import com.congty9a4.backend.dto.req.LoginRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.util.SecurityUtils;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Value("${google.client-id}")
    private String googleClientId;

    public AuthResponse loginWithGoogle(String googleToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId)).build();
            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken == null) {
                throw new RuntimeException("Token Google invalid!");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            // String pictureUrl = (String) payload.get("picture");

            var user = userRepository.findByEmail(email).orElseGet(() -> {

                String baseUsername = email;
                String randomPassword = java.util.UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(randomPassword);

                Userchan newUser = Userchan.builder()
                        .email(email)
                        .username(baseUsername)
                        .password(encodedPassword)
                        .isActive(true)
                        .build();

                return userRepository.save(newUser);
            });

            var accessToken = jwtService.createToken(user.getId().toString(), true);
            var refreshToken = jwtService.createToken(user.getId().toString(), false);

            return AuthResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Authentication Error Google: " + e.getMessage());
        }
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
        jwtService.validateToken(refreshToken);
        return AuthResponse.builder()
                .token(jwtService.createToken(SecurityUtils.getCurrentUserId(), true))
                .build();
    }

}
