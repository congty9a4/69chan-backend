package com.congty9a4.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(toEmail);
            message.setSubject("[69chan] Your Account Verification Code");
            message.setText("Welcome to 69chan!\n\n" +
                    "Your account verification OTP is: " + otp + "\n" +
                    "This code will expire in 5 minutes.\n\n" +
                    "Please do not share this code with anyone.");

            mailSender.send(message);
            // log.info("Đã gửi email OTP thành công đến: {}", toEmail);
        } catch (Exception e) {
            log.error("Error when sending OTP email {}: {}", toEmail, e.getMessage());
        }
    }
}
