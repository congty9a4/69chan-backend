package com.congty9a4.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
