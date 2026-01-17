package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.user.UserCreationRequest;
import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.AppPageable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Userchan userchan;

    private List<Userchan> userchanList;

    private UserResponse userResponse;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        String username = "testuser";
        String email = "testemail@chan";

        userchan = Userchan.builder()
                .id(userId)
                .username(username)
                .password("password")
                .email(email)
                .build();

        userResponse = UserResponse.builder()
                .email(email)
                .id(userId)
                .username(username)
                .build();


        userchanList = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Userchan user = Userchan.builder()
                    .id(UUID.randomUUID())
                    .username("user" + i)
                    .password("password" + i)
                    .email("user" + i + "@example.com")
                    .build();
                userchanList.add(user);
        }
    }




    @Test
    void createUser_validRequest_success() throws Exception {
        // given
        UserCreationRequest request = UserCreationRequest.builder()
                .email(userchan.getEmail())
                .username(userchan.getUsername())
                .password(userchan.getPassword())
                .build();

        when(userService.createUser(any(UserCreationRequest.class))).thenReturn(userchan);
        when(userMapper.toUserResponse(any(Userchan.class))).thenReturn(userResponse);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId.toString()))
                .andExpect(jsonPath("$.data.username").value(userchan.getUsername()));
    }

    @Test
    void updateUser_validRequest_success() throws Exception {
        // given
        UserUpdationRequest request = new UserUpdationRequest();


        when(userService.updateUser(any(UUID.class), any(UserUpdationRequest.class))).thenReturn(userchan);
        when(userMapper.toUserResponse(any(Userchan.class))).thenReturn(userResponse);

        // when & then
        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId.toString()))
                .andExpect(jsonPath("$.data.username").value(userchan.getUsername()));
    }

    @Test
    void getAllUsers_withPagination_validRequest_success() throws Exception {
        // given

        when(userMapper.toUserResponse(any(Userchan.class))).thenReturn(
                UserResponse.builder()
                        .id(userchan.getId())
                        .username(userchan.getUsername())
                        .email(userchan.getEmail())
                        .build()
        );

        List<UserResponse> userRespList = userchanList.stream().map(
                userMapper::toUserResponse
        ).toList();

        PageResponse<List<UserResponse>> pageResponse = new PageResponse<>(userRespList, 1, 10, userRespList.size(), 10, null, null);

        when(userService.getAllUsers(any(AppPageable.class))).thenReturn(pageResponse);
        // when & then
        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalItems").value(userRespList.size()))
                .andExpect(jsonPath("$.data.content[0].id").value(userRespList.getFirst().getId().toString()))
                .andExpect(jsonPath("$.data.content[0].username").value(userRespList.getFirst().getUsername()));

    }
}
