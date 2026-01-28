package com.congty9a4.backend.entity.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostMedia {
    String url;

    @JsonProperty("type")
    String mediaType;

    @JsonProperty("uploaded_at")
    OffsetDateTime uploadedAt;
}
