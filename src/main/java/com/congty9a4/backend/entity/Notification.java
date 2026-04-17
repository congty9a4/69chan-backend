package com.congty9a4.backend.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.congty9a4.backend.entity.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "notifications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    private String id;

    private String senderId;
    private String receiverId;

    private NotificationType type;

    private String targetId;
    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;

}
