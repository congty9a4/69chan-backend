package com.congty9a4.backend.service.implement;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.congty9a4.backend.entity.Notification;
import com.congty9a4.backend.entity.enums.NotificationType;
import com.congty9a4.backend.repository.mongo.NotificationRepository;
import com.congty9a4.backend.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Async
    @Override
    public void sendNotification(String senderId, String receiverId, NotificationType type, String targetId) {
        if (senderId.equals(receiverId))
            return;

        String message = "";
        switch (type) {
            case LIKE_POST -> message = "liked your article.";
            case COMMENT_POST -> message = "commented on your post.";
            case FRIEND_REQUEST -> message = "sent a friend request.";
            case ACCEPT_FRIEND -> message = "accepted the friend request.";
        }

        // Tạo Entity
        Notification noti = Notification.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .type(type)
                .targetId(targetId)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(noti);
        log.info("Saved notification: User {} -> User {}", senderId, receiverId);
    }
}
