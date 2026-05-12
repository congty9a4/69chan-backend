package com.congty9a4.backend.controller;

import com.congty9a4.backend.constant.LOCALE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("api/utils")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Utility", description = "Utility endpoints for testing and development purposes")
public class UtilityController {

    @GetMapping("/time")
    public Map<String, OffsetDateTime> getCurrentTime() {
        return Map.of("time", LOCALE.now);
    }
}

