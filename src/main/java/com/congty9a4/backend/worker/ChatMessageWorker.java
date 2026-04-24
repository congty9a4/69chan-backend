package com.congty9a4.backend.worker;

import com.congty9a4.backend.dto.req.message.MessageCreateRequest;
import com.congty9a4.backend.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageWorker {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    @Scheduled(fixedDelay = 1000)
    public void processMessageQueue() {
        // Pop message from the right side of the Redis list (Queue)
        String jsonMessage = redisTemplate.opsForList().rightPop("queue:chat_messages", 1, TimeUnit.SECONDS);

        if (jsonMessage != null) {
            try {
                // Deserialize JSON string into MessageCreateRequest
                MessageCreateRequest request = objectMapper.readValue(jsonMessage, MessageCreateRequest.class);
                log.info("Worker picked up a message from Redis. Calling service to save to DB...");

                // Call the @Async save method
                messageService.save(request).thenAccept(response -> {
                    // log.info("Successfully saved message to MongoDB! ID = {}", response.getId());
                }).exceptionally(ex -> {
                    log.error("Error saving message to DB!", ex);
                    return null;
                });

            } catch (Exception e) {
                log.error("Worker error while parsing JSON from Redis", e);
            }
        }
    }
}
