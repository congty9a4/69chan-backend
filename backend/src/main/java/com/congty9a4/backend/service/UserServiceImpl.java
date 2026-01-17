package com.congty9a4.backend.service;

import com.congty9a4.backend.config.TrackExecutionTime;
import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.exception.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.PaginationHelper;
import com.congty9a4.backend.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaginationHelper paginationHelper;

    @Override
    public PageResponse<List<UserResponse>> getAllUsers(AppPageable pageable) {
        var currentPage = userRepository.findAll(pageable.getPageable());

        return paginationHelper.buildPageResponse(
                currentPage,
                userMapper::toUserResponse,
                pageable
        );
    }

    @TrackExecutionTime
    @Override
    public Userchan getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));
    }

    @Override
    public Userchan createUser(UserCreationRequest userReq) {

        Userchan user = Userchan.builder()
                .username(userReq.getUsername())
                .password(userReq.getPassword())
                .email(userReq.getEmail()).
                build();

        String rawPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    @Override
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
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Userchan getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found with email: " + email));
    }
}

