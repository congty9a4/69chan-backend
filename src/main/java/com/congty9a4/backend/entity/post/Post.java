package com.congty9a4.backend.entity.post;

import com.congty9a4.backend.entity.Comment;
import com.congty9a4.backend.entity.enums.PostVisibility;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
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
@Document("posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    String id;

    @Field("user_id")
    String userId;

    @TextIndexed
    @Field("caption")
    String caption;

    @Field("tags")
    Set<String> tags;

    @Field("media")
    Set<PostMedia> mediaFiles;

    @Field("likes")
    Set<String> likes;

    @Field("like_count")
    @Builder.Default
    int likeCount = 0;

    @Field("comment_count")
    @Builder.Default
    int commentCount = 0;

    @Builder.Default
    @Field("visibility")
    PostVisibility visibility = PostVisibility.FRIENDS;

    @Field("is_deleted")
    @Builder.Default
    boolean isDeleted = false;

    @CreatedDate
    @Field("created_at")
    OffsetDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    OffsetDateTime updatedAt;
}
