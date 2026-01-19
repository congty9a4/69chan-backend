package com.congty9a4.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {

    String bio;

    String country;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid birthday format, should be YYYY-MM-DD")
    String birthday;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    String phone;

    String fullname;

}
