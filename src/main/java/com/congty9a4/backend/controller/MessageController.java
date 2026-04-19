package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.message.MessageCreateRequest;
import com.congty9a4.backend.dto.req.message.MessageUpdateRequest;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message CRUD APIs")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @PostMapping("/blank")
    public ApiResponse<MessageResponse> createBlank() {
        MessageCreateRequest request = new MessageCreateRequest();
        request.setSenderId("system");
        request.setReceiverId("user");
        request.setContent("");

        return ApiResponse.success(messageService.save(request).join());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get message by id")
    public ApiResponse<MessageResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(messageService.getById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List messages for user")
    public ApiResponse<List<MessageResponse>> listByUser(@PathVariable String userId) {
        return ApiResponse.success(messageService.listByUser(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update message")
    public ApiResponse<MessageResponse> update(@PathVariable Long id, @Valid @RequestBody MessageUpdateRequest request) {
        return ApiResponse.success(messageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete message")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        messageService.delete(id);
        return ApiResponse.success(null);
    }
}

