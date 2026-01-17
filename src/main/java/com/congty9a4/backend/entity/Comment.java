package com.congty9a4.backend.entity;

import com.congty9a4.backend.entity.post.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    String id;

    @Field("user_id")
    String userId;

    @DocumentReference
    Post post;

    @DocumentReference
    Set<Comment> childComments;

    String text;

    @Field("parent_id")
    String parentId;

    @Field("is_deleted")
    @Builder.Default
    boolean isDeleted = false;


    @CreatedDate
    @JsonProperty("created_at")
    OffsetDateTime createdAt;

    @LastModifiedDate
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt;
}
