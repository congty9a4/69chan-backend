package com.congty9a4.backend.service.conversations;


import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.entity.Conversation;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.mapper.ConversationMapper;
import com.congty9a4.backend.mapper.MessageMapper;
import com.congty9a4.backend.repository.mongo.ConversationRepository;
import com.congty9a4.backend.repository.mongo.MessageRepository;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.service.implement.ConversationServiceImpl;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static lombok.AccessLevel.PRIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Conversation Service Test")
@FieldDefaults(level = PRIVATE)
class ConversationServiceTest {

    @Mock
    ConversationRepository conversationRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MessageMapper messageMapper;

    @Mock
    ConversationMapper conversationMapper;

    @Mock
    UserService userService;

    @InjectMocks
    ConversationServiceImpl conversationService;


    @Test
    void getConversationHistory_withNullCursor_shouldReturnLastMessageId() {

        long lastMessageId = 123456;
        CursorPageRequest<Long> pageRequest = CursorPageRequest.<Long>builder().
                limit(100).build();

        String currentUserId = UUID.randomUUID().toString();

        String convId = UUID.randomUUID().toString();

        Conversation conv = Conversation.builder()
                .id(convId)
                .lastMessageId(lastMessageId)
                .participantIds(Arrays.asList(currentUserId))
                .build();

        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(conversationRepository.findById(convId)).thenReturn(Optional.ofNullable(conv));
            when(messageRepository.retrieveHistory(convId, lastMessageId, pageRequest.toPageRequest())).thenReturn(new ArrayList<>());

            conversationService.getConversationHistory(convId, pageRequest);

            assertEquals(lastMessageId, pageRequest.getCursor());
            Mockito.verify(conversationRepository, Mockito.times(1)).findById(convId);
        }

    }

    @Test
    void getConv_whenUserNotParticipant_shouldThrowException() {
        String convId = "convId";
        String currentUserId = "user1";
        String otherUserId = "user2";

        Conversation conv = Conversation.builder()
                .id(convId)
                .participantIds(Collections.singletonList(otherUserId)) // User 1 is not here
                .build();

        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(conversationRepository.findById(convId)).thenReturn(Optional.of(conv));

            AppException ex = assertThrows(AppException.class, () -> conversationService.retrieveConversationById(convId));
            assertEquals(ErrorCode.CANNOT_ACCESS_CONVERSATION, ex.getErrorCode());
        }
    }
}
