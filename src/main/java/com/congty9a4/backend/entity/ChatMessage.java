package com.congty9a4.backend.entity;

import com.congty9a4.backend.entity.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String roomId;

    private String senderId;

    private String content;

    private MessageType type;

    private List<String> readBy;

    @CreatedDate
    private LocalDateTime createdAt;
}