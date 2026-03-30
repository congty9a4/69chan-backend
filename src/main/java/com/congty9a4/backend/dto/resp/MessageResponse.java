package com.congty9a4.backend.dto.resp;

import java.time.OffsetDateTime;

public record MessageResponse(
        Long id,
        String senderId,
        String receiverId,
        String content,
        OffsetDateTime createdAt
) {}

