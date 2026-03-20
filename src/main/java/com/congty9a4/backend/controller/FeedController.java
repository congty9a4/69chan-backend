package com.congty9a4.backend.controller;


import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.service.FanoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/feeds")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Tag(name = "Feeds API")
public class FeedController {

    FanoutService fanoutService;


    @GetMapping
    @Operation(summary = "Get home feed with cursor-based pagination")
    public CursorPageResponse<PostResponse> getHomeFeed(
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "cursor in date-time format", example = "2026-03-18T22:14:19.123+07:00")
            @RequestParam(required = false) String after) {


        CursorPageRequest pageRequest = CursorPageRequest.builder()
                .limit(limit)
                .cursor(after)
                .build();

        return fanoutService.getHomeFeed(pageRequest);
    }


}
