package com.congty9a4.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

