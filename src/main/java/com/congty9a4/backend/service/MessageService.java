package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.message.MessageCreateRequest;
import com.congty9a4.backend.dto.req.message.MessageUpdateRequest;
import com.congty9a4.backend.dto.resp.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse create(MessageCreateRequest request);

    MessageResponse getById(Long id);

    List<MessageResponse> listByUser(String userId);

    MessageResponse update(Long id, MessageUpdateRequest request);

    void delete(Long id);
}

