package com.congty9a4.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {

    @Size(min = 5, max = 255)
    String fullName;

    @Size(max = 500)
    String bio;

    @URL
    String avatarUrl;

    @URL
    String coverPhotoUrl;

    @Size(max = 255)
    String location;

    LocalDate birthday;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    String phone;
}

