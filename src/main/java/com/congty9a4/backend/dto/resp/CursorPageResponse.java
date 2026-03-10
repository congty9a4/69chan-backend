package com.congty9a4.backend.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CursorPageResponse<T> {
    List<T> data;
    PageInfo pageInfo;

    public CursorPageResponse(List<T> data, PageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PageInfo {
        String nextCursor;
        String prevCursor;
        boolean hasNext;
        boolean hasPrev;
    }
}

