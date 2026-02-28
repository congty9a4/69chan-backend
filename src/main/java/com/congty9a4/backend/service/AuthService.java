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

}
