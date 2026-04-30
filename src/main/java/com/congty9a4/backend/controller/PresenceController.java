package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/presence")
@RequiredArgsConstructor
public class PresenceController {

    private final PresenceService presenceService;

    @GetMapping("/status")
    public ApiResponse<Map<String, Boolean>> getStatus(@RequestParam List<String> userIds) {
        return ApiResponse.success(presenceService.getUsersPresence(userIds));
    }
}