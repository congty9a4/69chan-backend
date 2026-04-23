package com.congty9a4.backend.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("page_info")
    PageInfo pageInfo;

    public CursorPageResponse(List<T> data, PageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PageInfo<T> {
        @JsonProperty("next_cursor")
        T nextCursor;

        @JsonProperty("has_next")
        boolean hasNext;

    }
}

