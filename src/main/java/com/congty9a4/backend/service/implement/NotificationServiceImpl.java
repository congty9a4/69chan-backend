package com.congty9a4.backend.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.congty9a4.backend.dto.resp.NotificationResponse;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.Notification;
import com.congty9a4.backend.entity.enums.NotificationType;
import com.congty9a4.backend.repository.mongo.NotificationRepository;
import com.congty9a4.backend.service.NotificationService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

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

    @Override
    public List<NotificationResponse> getNotification() {
        String currentUserId = SecurityUtils.getCurrentUserId();
        List<Notification> rawNotifications = notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(currentUserId);

        // return
        // notificationRepository.findByReceiverIdOrderByCreatedAtDesc(currentUserId);
        return rawNotifications.stream().map(noti -> {
            // Lấy thông tin của người thả tim/comment (UserA)
            Infochan senderInfo = userService.userInfo(noti.getSenderId());

            return NotificationResponse.builder()
                    .id(noti.getId())
                    .sender(senderInfo) // Gắn thông tin UserA vào đây
                    .type(noti.getType().name())
                    .targetId(noti.getTargetId())
                    .message(noti.getMessage())
                    .createdAt(noti.getCreatedAt())
                    .read(noti.isRead())
                    .build();
        }).toList();
    }

    @Override
    public long getUnreadCountNotification() {
        String currentUserId = SecurityUtils.getCurrentUserId();
        return notificationRepository.countByReceiverIdAndIsReadFalse(currentUserId);
    }

    @Override
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Not Found Notification!"));
        String currentUserId = SecurityUtils.getCurrentUserId();
        if (!notification.getReceiverId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên thông báo này!");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
