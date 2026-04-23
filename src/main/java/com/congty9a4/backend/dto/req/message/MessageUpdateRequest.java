package com.congty9a4.backend.dto.req.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageUpdateRequest {
    @NotBlank
    private String content;
}
