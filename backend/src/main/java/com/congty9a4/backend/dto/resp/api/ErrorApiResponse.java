package com.congty9a4.backend.dto.resp.api;


import com.congty9a4.backend.constant.CustomLocale;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorApiResponse {

    @Builder.Default
    OffsetDateTime timestamp = CustomLocale.now;
    // HTTP Status Code
    int code;
    String message;
    String detail;

}
