package com.congty9a4.backend.controller;

import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/samples")
public class SampleController {

    @GetMapping
    public Map<String, String> getSample() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a sample endpoint");
        return response;
    }

}

