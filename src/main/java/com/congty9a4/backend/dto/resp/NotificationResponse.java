package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.Infochan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private Infochan sender;
    private String type;
    private String targetId;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;
}