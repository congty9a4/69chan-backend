package com.congty9a4.backend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents user with basic info
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Infochan {

    @JsonProperty("avatar")
    String profilePicture;

    String username;

    @JsonProperty("id")
    String userId;

    @JsonProperty("fullname")
    String keyName;

}
