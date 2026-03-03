package com.congty9a4.backend.dto.req;

import com.congty9a4.backend.entity.enums.NotificationType;

import lombok.Data;

@Data
public class NotificationRequest {
    private String senderId;
    private String receiverId;
    private NotificationType type;
    private String targetId;
}
