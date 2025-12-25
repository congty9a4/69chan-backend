package com.congty9a4.backend.entity;

import com.congty9a4.backend.entity.enums.PostVisibility;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("posts")
public class Post {
    @Id
    private UUID id;

    @Field("author_id")
    private UUID authorId;

    private String content;

    @Field("image_urls")
    private Set<String> imageUrls;

    @Field("video_url")
    private Set<String> videoUrls;

    private PostVisibility visibility;

    @Field("is_deleted")
    private Boolean isDeleted;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
}
