package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.NotificationRequest;
import com.congty9a4.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/test")
    public String createTestNotification(@RequestBody NotificationRequest request) {
        // Gọi service để lưu
        notificationService.sendNotification(
                request.getSenderId(),
                request.getReceiverId(),
                request.getType(),
                request.getTargetId());
        return "Đã gửi thông báo thành công! Check MongoDB đi bạn ơi.";
    }
}