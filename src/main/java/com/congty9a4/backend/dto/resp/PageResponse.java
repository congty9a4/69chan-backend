package com.congty9a4.backend.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(
    T content,
    int page,
    int size,
    long totalItems,
    int totalPages,
    String next,
    String prev
) { }
