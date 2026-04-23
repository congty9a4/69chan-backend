package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.WsAckMessage;
import com.congty9a4.backend.dto.resp.WsChatMessage;
import com.congty9a4.backend.dto.resp.WsTypingIndicator;
import com.congty9a4.backend.entity.enums.MessageStatus;
import com.congty9a4.backend.service.MessageQueueService;
import com.congty9a4.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Slf4j
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

        // Tạo 1 ID duy nhất để tracking tin nhắn (dùng chung cho cả luồng)
        String messageId = UUID.randomUUID().toString();

        // 1. Đẩy vào Redis Queue (Chuyển việc lưu DB cho Dev 2)
        messageQueueService.pushToQueue(senderId, payload);

        // 2. Gửi Real-time cho người nhận
        messagingTemplate.convertAndSendToUser(
                payload.getReceiverId(),
                "/queue/messages",
                payload);

        // 3. Phản hồi ACK (Đã gửi) cho người gửi bằng Object (Chuẩn bài nhất)
        WsAckMessage ack = new WsAckMessage();
        ack.setMessageId(messageId);
        ack.setStatus(MessageStatus.SENT); // Hoặc MessageStatus.SENT tùy enum của fen
        messagingTemplate.convertAndSendToUser(senderId, "/queue/acks", ack);

        log.info("Đã phát sóng tin nhắn từ {} tới {} trên kênh WebSocket", senderId, payload.getReceiverId());
    }

    // Hiển thị đang gõ...
    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload WsTypingIndicator payload, Principal principal) {
        messagingTemplate.convertAndSendToUser(
                payload.getReceiverId(), "/queue/typing", payload);
    }
}