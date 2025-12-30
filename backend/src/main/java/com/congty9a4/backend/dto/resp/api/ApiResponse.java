package com.congty9a4.backend.dto.resp.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    int status;
    String message;
    T data;

    public static <T> ApiResponse<T> success(String message, T body) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(!message.isBlank() ? message : "Success")
                .data(body)
                .build();
    }
}
