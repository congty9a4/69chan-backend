package com.congty9a4.backend.entity.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/*
    * Infochan entity representing userchan with main property.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostMedia {
    String url;

    @JsonProperty("type")
    String mediaType;

    @JsonProperty("uploaded_at")
    OffsetDateTime uploadedAt;
}
