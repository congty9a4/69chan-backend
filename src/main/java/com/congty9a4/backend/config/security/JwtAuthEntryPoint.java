package com.congty9a4.backend.config.security;

import com.congty9a4.backend.dto.resp.api.ErrorApiResponse;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("ERROR: {}", authException.getMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));

        var resp = ErrorApiResponse.builder()
                .message("Unauthorized access")
                .detail(ErrorCode.UNAUTHENTICATED.getDetailedMessage())
                .timestamp(null)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(resp));
        response.flushBuffer();
    }
}
