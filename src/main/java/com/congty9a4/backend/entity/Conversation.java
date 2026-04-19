package com.congty9a4.backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("conversations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation {
    String id;

    @Field("participant_ids")
    List<String> participantIds;

    @Field("last_message")
    String lastMessage;

    @Field("last_message_id")
    Long lastMessageId;

    @Field("last_sender_id")
    String lastSenderId;

    @Field("last_message_time")
    Long lastMessageTime;

    int unreadCount;
}
