package com.congty9a4.backend.dto.resp;


import com.congty9a4.backend.entity.Infochan;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    @JsonProperty("access_token")
    String token;

    @JsonProperty("refresh_token")
    String refreshToken;

    Infochan user;
}
