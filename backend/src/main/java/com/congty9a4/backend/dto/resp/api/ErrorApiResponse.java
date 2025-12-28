package com.congty9a4.backend.dto.resp.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorApiResponse {

    @Builder.Default
    ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    // HTTP Status Code
    int code;
    String message;
    String detail;

}
