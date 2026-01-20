package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.Userchan;
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
public class SearchResponse {
    @JsonProperty("users")
    List<UserResponse> users;

    @JsonProperty("posts")
    List<PostResponse> posts;

}

