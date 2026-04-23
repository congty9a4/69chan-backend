package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.ConversationResponse;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.entity.Conversation;

import java.util.List;

public interface ConversationService {

    CursorPageResponse<MessageResponse> getConversationHistory(String id, CursorPageRequest<Long> pageRequest);

    List<ConversationResponse> getAllMyChats();

    ConversationResponse retrieveConversationById(String id);
}
