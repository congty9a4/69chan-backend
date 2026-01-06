package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.post.Infochan;
import com.congty9a4.backend.entity.post.PostMedia;
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
public class PostResponse {
    String id;
    // List<Comment> comments;

    List<String> tags;

    @JsonProperty("medias")
    List<PostMedia> mediaFiles ;

    @JsonProperty("user")
    Infochan infochan;

    @JsonProperty("scope")
    String scope;

    String caption;

    @JsonProperty("created_at")
    OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt;
}
