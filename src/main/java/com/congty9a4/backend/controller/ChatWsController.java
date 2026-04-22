package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.WsAckMessage;
import com.congty9a4.backend.dto.resp.WsChatMessage;
import com.congty9a4.backend.dto.resp.WsTypingIndicator;
import com.congty9a4.backend.entity.enums.MessageStatus;
import com.congty9a4.backend.service.MessageQueueService;
import com.congty9a4.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageQueueService messageQueueService;
    private final PresenceService presenceService;

    // Heartbeat (Ping từ Client)
    @MessageMapping("/presence.heartbeat")
    public void receiveHeartbeat(Principal principal) {
        if (principal != null) {
            presenceService.handleHeartbeat(principal.getName());
        }
    }

    // Gửi tin nhắn
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload WsChatMessage payload, Principal principal) {
        String senderId = principal.getName();
        String messageId = UUID.randomUUID().toString();

        // 1. Gửi Real-time cho người nhận
        messagingTemplate.convertAndSendToUser(
                payload.getReceiverId(), "/queue/messages", payload);

        // 2. Phản hồi ACK (Đã gửi) cho người gửi
        WsAckMessage ack = new WsAckMessage();
        ack.setMessageId(messageId);
        ack.setStatus(MessageStatus.SENT);
        messagingTemplate.convertAndSendToUser(senderId, "/queue/acks", ack);

        // 3. Đẩy vào Redis Queue (Chuyển việc cho Dev 2)
        messageQueueService.pushToQueue(senderId, payload);
    }

    // Hiển thị đang gõ...
    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload WsTypingIndicator payload, Principal principal) {
        messagingTemplate.convertAndSendToUser(
                payload.getReceiverId(), "/queue/typing", payload);
    }
}