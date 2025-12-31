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
public class ApiResponse<T> {

    @Builder.Default
    boolean isSuccess = true;

    T data;

    @Builder.Default
    OffsetDateTime timestamp = CustomLocale.now;

    public static <T> ApiResponse<T> success(T body) {
        return ApiResponse.<T>builder()
                .data(body)
                .build();
    }
}
