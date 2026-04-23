package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.ConversationResponse;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.entity.Conversation;
import com.congty9a4.backend.entity.Message;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.mapper.ConversationMapper;
import com.congty9a4.backend.mapper.MessageMapper;
import com.congty9a4.backend.repository.mongo.ConversationRepository;
import com.congty9a4.backend.repository.mongo.MessageRepository;
import com.congty9a4.backend.service.ConversationService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ConversationServiceImpl implements ConversationService {

    UserService userService;
    ConversationRepository conversationRepository;
    MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ConversationMapper conversationMapper;


    @Override
    public CursorPageResponse<MessageResponse> getConversationHistory(String id, CursorPageRequest<Long> pageRequest) {
        Conversation conv = getConv(id);

        if (pageRequest.getCursor() == null) {
            pageRequest.setCursor(conv.getLastMessageId());
        }

        List<MessageResponse> messages = messageRepository.retrieveHistory(id, pageRequest.getCursor(), PageRequest.of(0, pageRequest.getLimit() + 1))
                .stream().map(messageMapper::toMessageResponse).toList();

        var pageInfo = CursorPageResponse.PageInfo.<Long>builder().nextCursor(messages.getLast().id()).build();

        return CursorPageResponse.<MessageResponse>builder()
                .data(messages)
                .pageInfo(pageInfo)
                .build();
    }

    @Override
    public List<ConversationResponse> getAllMyChats() {
        String userId = SecurityUtils.getCurrentUserId();

        userService.getUserById(UUID.fromString(userId));
        return conversationRepository.findAllMyChats(userId).stream().map(conversationMapper::toResponse).toList();
    }

    @Override
    public ConversationResponse retrieveConversationById(String id) {
        return conversationMapper.toResponse(getConv(id));
    }

    private Conversation getConv(String id) {
        return conversationRepository.findById(id).map(e -> {
                    if (!checkConversationAccess(e)) throw new AppException(ErrorCode.CANNOT_ACCESS_CONVERSATION);
                    return e;
                } )
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
    }

    private Boolean checkConversationAccess(Conversation conversation) {
        String userId = SecurityUtils.getCurrentUserId();
        return conversation.getParticipantIds().contains(userId);
    }
}
