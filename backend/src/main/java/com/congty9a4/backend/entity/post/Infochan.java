package com.congty9a4.backend.entity.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/*
    * Infochan entity representing userchan with main property.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Infochan {

    @JsonProperty("profile_picture")
    String profilePicture;

    String username;

    @JsonProperty("id")
    String userId;

    String fullname;

}
