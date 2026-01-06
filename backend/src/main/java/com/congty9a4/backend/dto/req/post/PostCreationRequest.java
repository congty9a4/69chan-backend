package com.congty9a4.backend.dto.req.post;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {

    String caption;

    Set<String> tags;

    @JsonProperty("images")
    Set<String> imageUrls;

    @JsonProperty("videos")
    Set<String> videoUrls;

    @JsonProperty("is_public")
    boolean isPublic;



}
