package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.Infochan;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {

    Integer id;

    @JsonProperty("user")
    Infochan infochan;

    String bio;

    @JsonProperty("avatar_url")
    String avatarUrl;

    @JsonProperty("cover_photo_url")
    String coverPhotoUrl;

    String location;

    LocalDate birthday;

    String phone;

    @JsonProperty("created_at")
    OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt;
}

