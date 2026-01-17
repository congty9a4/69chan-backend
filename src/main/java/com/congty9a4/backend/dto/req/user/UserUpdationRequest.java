package com.congty9a4.backend.dto.req.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdationRequest {

    @Size(min = 3, max = 50)
    String username;

    @Email
    String email;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    String phone;

}
