package com.congty9a4.backend.dto.resp.api;


import com.congty9a4.backend.constant.LOCALE;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorApiResponse {

    @Builder.Default
    boolean isSuccess = false;

    int status;

    String message;

    String detail;

    Map<String, String> errors;

    @Builder.Default
    OffsetDateTime timestamp = LOCALE.now;
}
