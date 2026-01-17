package com.congty9a4.backend.service;


import com.congty9a4.backend.config.security.JwtService;
import com.congty9a4.backend.constant.USER;
import com.congty9a4.backend.dto.req.LoginRequest;
import com.congty9a4.backend.dto.resp.AuthResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AuthResponse authenticate(LoginRequest req) {
        String email = req.getEmail();
        String requestedPassword = req.getPassword();

        Userchan user = userService.getUserByEmail(email);

        if (!passwordEncoder.matches(requestedPassword, user.getPassword()))
            throw new AppException(ErrorCode.INVALID_CREDENTIALS, "Wrong password");

        String token = jwtService.createToken(user.getId().toString());

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse guest() {
        Userchan guest = userService.getUserByEmail(USER.GUEST.EMAIL);
        String token = jwtService.createToken(guest.getId().toString());
        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toUserResponse(guest))
                .build();
    }
}
