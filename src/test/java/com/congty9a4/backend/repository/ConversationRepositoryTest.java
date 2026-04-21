package com.congty9a4.backend.repository;

import com.congty9a4.backend.config.mongodb.MongoConfig;
import com.congty9a4.backend.entity.Conversation;
import com.congty9a4.backend.repository.mongo.ConversationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Import(MongoConfig.class)
@ActiveProfiles("test")
class ConversationRepositoryTest {

    @Autowired
    ConversationRepository conversationRepository;

    @BeforeEach
    void setUp() {
        conversationRepository.deleteAll();


        conversationRepository.save(
                Conversation.builder()
                .id("conv-1")
                .participantIds(List.of("u1", "u2"))
                .lastMessage("hello")
                .lastMessageTime(1001L)
                .build());

        conversationRepository.save(Conversation.builder()
                .id("conv-2")
                .participantIds(List.of("u3", "u1"))
                .lastMessage("yo")
                        .lastMessageTime(1003L)
                .build());

        conversationRepository.save(Conversation.builder()
                .id("conv-3")
                .participantIds(List.of("u4", "u5"))
                .lastMessage("private")
                .lastMessageTime(1002L)
                .build());
    }

    @AfterEach
    void tearDown() {
        conversationRepository.deleteAll();
    }

    @Test
    void findAllMyChats_returnsOnlyConversationsContainingUser() {
        List<Conversation> chats = conversationRepository.findAllMyChats("u1");

        assertEquals(2, chats.size());
        assertTrue(chats.stream().anyMatch(conversation -> "conv-1".equals(conversation.getId())));
        assertTrue(chats.stream().anyMatch(conversation -> "conv-2".equals(conversation.getId())));
        assertEquals("conv-2", chats.getFirst().getId());
    }

    @Test
    void findAllMyChats_returnsEmptyWhenUserHasNoConversations() {
        List<Conversation> chats = conversationRepository.findAllMyChats("u999");

        assertTrue(chats.isEmpty());
    }
}

