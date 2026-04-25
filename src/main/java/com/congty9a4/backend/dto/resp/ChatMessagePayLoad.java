package com.congty9a4.backend.dto.resp;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagePayLoad {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private String type; // TEXT, IMAGE, FILE...
    private Long timestamp;
}
