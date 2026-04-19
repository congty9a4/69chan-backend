package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.req.message.MessageCreateRequest;
import com.congty9a4.backend.dto.req.message.MessageUpdateRequest;
import com.congty9a4.backend.dto.resp.ConversationResponse;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.entity.Conversation;
import com.congty9a4.backend.repository.mongo.ConversationRepository;
import com.congty9a4.backend.service.ConversationService;
import com.congty9a4.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@Tag(name = "Message", description = "Conversation APIs")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/{id}/history")
    @Operation(summary = "Get conversation history by user ID")
    public CursorPageResponse<MessageResponse> getConversationHistory(
            @PathVariable String id,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Long cursor) {

        CursorPageRequest<Long> pageRequest = CursorPageRequest.<Long>builder()
                .limit(limit)
                .cursor(cursor)
                .build();

        return conversationService.getConversationHistory(id, pageRequest);

    }

    @GetMapping("/me")
    @Operation(summary = "List conversations for current user")
    public ApiResponse<List<Conversation>> listMyConversations() {
        return ApiResponse.success(conversationService.getAllMyChats());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get conversation by ID")
    public ApiResponse<Conversation> getConversationById(@PathVariable String id) {
        return ApiResponse.success(conversationService.retrieveConversationById(id));
    }
}

