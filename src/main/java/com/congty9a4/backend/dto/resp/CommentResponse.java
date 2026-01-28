package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.Infochan;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;

    String text;

    @JsonProperty("post_id")
    String postId;

    @JsonProperty("user")
    Infochan infochan;

    @JsonProperty("children")
    List<CommentResponse> childComments;

    @JsonProperty("created_at")
    OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt;
}

