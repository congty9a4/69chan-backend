package com.congty9a4.backend.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
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
