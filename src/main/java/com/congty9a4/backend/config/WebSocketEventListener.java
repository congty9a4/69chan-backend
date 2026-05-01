package com.congty9a4.backend.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.congty9a4.backend.service.PresenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final PresenceService presenceService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (event.getUser() != null) {
            String userId = event.getUser().getName();
            presenceService.handleHeartbeat(userId);
            log.info(">>>> [Redis] User {} has joined the system", userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        if (event.getUser() != null) {
            String userId = event.getUser().getName();
            presenceService.markAsOffline(userId);
            log.info(">>>> [Redis] User {} has left", userId);
        }
    }
}