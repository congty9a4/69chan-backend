package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {

    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String PRESENCE_PREFIX = "presence:user:";
    private static final Duration TTL = Duration.ofSeconds(30);

    @Override
    public void handleHeartbeat(String userId) {
        boolean wasOffline = Boolean.FALSE.equals(redisTemplate.hasKey(PRESENCE_PREFIX + userId));
        redisTemplate.opsForValue().set(PRESENCE_PREFIX + userId, "ONLINE", TTL);

        if (wasOffline) {
            broadcastPresence(userId, "ONLINE");
        }
    }

    @Override
    public Map<String, Boolean> getUsersPresence(List<String> userIds) {
        return userIds.stream().collect(Collectors.toMap(
                id -> id,
                id -> Boolean.TRUE.equals(redisTemplate.hasKey(PRESENCE_PREFIX + id))));
    }

    @Override
    public void markAsOffline(String userId) {
        redisTemplate.delete(PRESENCE_PREFIX + userId);
        broadcastPresence(userId, "OFFLINE");
    }

    private void broadcastPresence(String userId, String status) {
        String payload = String.format("{\"userId\":\"%s\", \"status\":\"%s\"}", userId, status);
        messagingTemplate.convertAndSend("/topic/presence", payload);
    }
}