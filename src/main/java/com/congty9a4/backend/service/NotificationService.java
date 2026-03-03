package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.enums.NotificationType;

public interface NotificationService {
    void sendNotification(String senderId, String receiverId, NotificationType type, String target);
}
