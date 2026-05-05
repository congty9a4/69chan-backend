package com.congty9a4.backend.dto.req.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageCreateRequest {
    @NotBlank
    private String senderId;

    @NotBlank
    private String receiverId;

    @NotBlank
    private String content;

    @NotBlank
    String conversationId;
}
