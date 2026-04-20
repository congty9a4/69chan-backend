package com.congty9a4.backend.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record MessageResponse(
        Long id,

        @JsonProperty("sender_id")
        String senderId,

        @JsonProperty("receiver_id")
        String receiverId,

        String content,

        @JsonProperty("created_at")
        OffsetDateTime createdAt
) {}

