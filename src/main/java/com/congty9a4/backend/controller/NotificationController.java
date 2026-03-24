package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.NotificationRequest;
import com.congty9a4.backend.dto.resp.NotificationResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.entity.Notification;
import com.congty9a4.backend.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get List Notification", description = "Get all notifications of the current user")
    public ApiResponse<List<NotificationResponse>> getMyNotifications() {
        return ApiResponse.success(notificationService.getNotification());
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get the number of unread notifications", description = "Used to display the number of unread notifications in red on the UI")
    public ApiResponse<Long> getUnreadCountNotification() {
        return ApiResponse.success(notificationService.getUnreadCountNotification());
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark as read", description = "Set isRead = true for a specific notification")
    public ApiResponse<String> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ApiResponse.success("Marked as read successfully!");
    }
}