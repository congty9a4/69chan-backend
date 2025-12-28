package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @GetMapping("/endpoint")
    public Map<String, String> getSample() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a sample endpoint");
        return response;
    }

    @GetMapping("/error")
    public void getError() {
        throw new RuntimeException("This is a sample error");
    }

    @GetMapping("/user")
    public ApiResponse<Userchan> getSampleUser() {
        return ApiResponse.<Userchan>builder()
                .data(new Userchan())
                .message("Sample user data")
                .build();
    }
}

