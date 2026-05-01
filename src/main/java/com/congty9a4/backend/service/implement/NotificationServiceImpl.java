package com.congty9a4.backend.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.congty9a4.backend.dto.resp.NotificationResponse;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.Notification;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.enums.NotificationType;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.NotificationRepository;
import com.congty9a4.backend.service.NotificationService;
import com.congty9a4.backend.service.PresenceService;
import com.congty9a4.backend.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private static final String UNREAD_KEY = "unread_count:";
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceService presenceService;

    private final StringRedisTemplate redisTemplate;

    private final UserMapper userMapper;

    @Async
    @Override
    public void sendNotification(String senderId, String receiverId, NotificationType type, String targetId) {
        if (senderId.equals(receiverId))
            return;

        String message = switch (type) {
            case LIKE_POST -> "liked your article.";
            case COMMENT_POST -> "commented on your post.";
            case FOLLOW -> "started following you.";
            case MESSAGE -> "sent you a new message.";
            default -> "sent you a notification.";
        };

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
        String key = UNREAD_KEY + receiverId;
        try {
            redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, java.time.Duration.ofHours(12)); // TTL
        } catch (Exception e) {
            log.error("Redis increment failed", e);
        }
        log.info("Saved notification: User {} -> User {}", senderId, receiverId);

        Map<String, Boolean> presence = presenceService.getUsersPresence(List.of(receiverId));
        boolean isOnline = presence.getOrDefault(receiverId, false);
        if (isOnline) {
            try {
                UUID senderUUID = safeUUID(senderId);
                if (senderUUID == null) {
                    log.warn("Skip notification: invalid senderId {}", senderId);
                    return;
                }

                Userchan sender = userRepository.findById(senderUUID)
                        .orElseThrow(() -> new RuntimeException("Sender not found"));
                Infochan senderInfo = userMapper.toInfochan(sender);
                NotificationResponse response = NotificationResponse.builder()
                        .id(noti.getId())
                        .sender(senderInfo)
                        .type(type.name())
                        .targetId(targetId)
                        .message(message)
                        .createdAt(noti.getCreatedAt())
                        .read(false)
                        .build();

                messagingTemplate.convertAndSendToUser(
                        receiverId,
                        "/queue/notifications",
                        response);

                log.info("Push WS notification success to user: {}", receiverId);
            } catch (Exception e) {
                log.error("WebSocket push failed", e);
            }
        } else {
            log.info("User {} is offline. Skipped WS push. Saved to DB only.", receiverId);
        }

    }

    @Override
    public List<NotificationResponse> getNotification() {
        String currentUserId = SecurityUtils.getCurrentUserId();
        List<Notification> rawNotifications = notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(currentUserId);

        if (rawNotifications.isEmpty()) {
            return List.of();
        }

        Set<String> senderIds = rawNotifications.stream()
                .map(Notification::getSenderId)
                .collect(Collectors.toSet());

        List<UUID> uuids = senderIds.stream()
                .map(this::safeUUID)
                .filter(java.util.Objects::nonNull)
                .toList();

        List<Userchan> userEntities = userRepository.findAllById(uuids);
        List<Infochan> senders = userEntities.stream()
                .filter(user -> user.getId() != null)
                .map(userMapper::toInfochan)
                .toList();
        Map<String, Infochan> senderMap = senders.stream()
                .collect(Collectors.toMap(
                        Infochan::getUserId,
                        sender -> sender,
                        (existing, replacement) -> existing));

        return rawNotifications.stream().map(noti -> {
            Infochan senderInfo = senderMap.get(noti.getSenderId());

            return NotificationResponse.builder()
                    .id(noti.getId())
                    .sender(senderInfo)
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

        String userId = SecurityUtils.getCurrentUserId();
        String key = UNREAD_KEY + userId;

        String count = redisTemplate.opsForValue().get(key);

        if (count == null) {
            long dbCount = notificationRepository
                    .countByReceiverIdAndIsReadFalse(userId);

            redisTemplate.opsForValue().set(key, String.valueOf(dbCount));
            redisTemplate.expire(key, java.time.Duration.ofHours(12));

            return dbCount;
        }

        long value = Long.parseLong(count);

        if (value < 0 || value > 1000) {
            long dbCount = notificationRepository
                    .countByReceiverIdAndIsReadFalse(userId);

            redisTemplate.opsForValue().set(key, String.valueOf(dbCount));
            return dbCount;
        }

        return value;
    }

    @Transactional
    @Override
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Not Found Notification!"));

        String currentUserId = SecurityUtils.getCurrentUserId();
        if (!notification.getReceiverId().equals(currentUserId)) {
            throw new IllegalStateException("No permission");
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);

            String key = UNREAD_KEY + currentUserId;

            Long value = redisTemplate.opsForValue().decrement(key);

            if (value != null && value < 0) {
                redisTemplate.opsForValue().set(key, "0");
            }
        }
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            log.warn("Invalid UUID: {}", id);
            return null;
        }
    }
}
