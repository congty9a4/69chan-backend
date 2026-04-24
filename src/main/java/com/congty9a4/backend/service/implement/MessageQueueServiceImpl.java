package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.dto.req.message.MessageCreateRequest;
import com.congty9a4.backend.dto.resp.WsChatMessage;
import com.congty9a4.backend.service.MessageQueueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageQueueServiceImpl implements MessageQueueService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CHAT_QUEUE_KEY = "queue:chat_messages";

    @Override
    public void pushToQueue(String senderId, WsChatMessage wsMsg) {
        try {
            MessageCreateRequest dbReq = new MessageCreateRequest();
            dbReq.setSenderId(senderId);
            dbReq.setReceiverId(wsMsg.getReceiverId());
            dbReq.setContent(wsMsg.getContent());

            String jsonMessage = objectMapper.writeValueAsString(dbReq);
            redisTemplate.opsForList().leftPush(CHAT_QUEUE_KEY, jsonMessage);
            log.info("Đã đẩy tin nhắn của {} vào Redis Queue.", senderId);
        } catch (JsonProcessingException e) {
            log.error("Lỗi parse JSON khi đẩy vào Queue: {}", e.getMessage());
        }
    }
}