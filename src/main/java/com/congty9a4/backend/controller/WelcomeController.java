package com.congty9a4.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Welcome", description = "Welcome endpoint for the application")
public class WelcomeController {
    @GetMapping
    @Operation(summary = "Welcome message", description = "Returns a welcome message for the 69chan application")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to 69chan!");
    }
}
