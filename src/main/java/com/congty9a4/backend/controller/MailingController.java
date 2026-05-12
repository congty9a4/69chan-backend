package com.congty9a4.backend.controller;

import com.congty9a4.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MailingController {
    EmailService emailService;

    @GetMapping("/{email}")
    public String checkMail(@PathVariable String email){
        emailService.sendOtpEmail(email, "123456");
        return "ok";
    }

}
