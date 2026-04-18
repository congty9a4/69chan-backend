package com.congty9a4.backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("message")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    Long Id;

    @Field("sender_id")
    String senderId;

    @Field("receiver_id")
    String receiverId;

    @TextIndexed
    @Field("content")
    String content;

    @CreatedDate
    @Field("created_at")
    OffsetDateTime createdAt;
}
