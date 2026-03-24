package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.enums.NotificationType;

import java.util.List;

import com.congty9a4.backend.dto.resp.NotificationResponse;
import com.congty9a4.backend.entity.Notification;

public interface NotificationService {
    void sendNotification(String senderId, String receiverId, NotificationType type, String target);

    List<NotificationResponse> getNotification();

    long getUnreadCountNotification();

    void markAsRead(String notificationId);
}
