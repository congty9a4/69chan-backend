package com.congty9a4.backend.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Use when you need cursor-based pagination")
public class CursorPageRequest {

    @Schema(
            description = "Date",
            example = "2024-06-01T12:00:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    String cursor;

    @Builder.Default
    @Schema(
            description = "Number of items to return",
            example = "10"
    )
    int limit = 10;


    @Schema(
            description = "Direction of pagination, either 'NEXT' or 'PREV'",
            example = "NEXT"
    )
    @Builder.Default
    Direction direction = Direction.NEXT;

    public enum Direction {
        NEXT, PREV
    }
}

