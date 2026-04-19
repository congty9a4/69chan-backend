package com.congty9a4.backend.repository;

import com.congty9a4.backend.config.mongodb.MongoConfig;
import com.congty9a4.backend.entity.Message;
import com.congty9a4.backend.repository.mongo.MessageRepository;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Import(MongoConfig.class)
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void seedMessages() {
        mongoTemplate.dropCollection("messages");

        insertMessage(100L, "conv-1", "s1", "r1", "m100");
        insertMessage(101L, "conv-1", "s1", "r1", "m101");
        insertMessage(102L, "conv-1", "s1", "r1", "m102");
        insertMessage(103L, "conv-1", "s1", "r1", "m103");
        insertMessage(104L, "conv-1", "s1", "r1", "m104");
        insertMessage(105L, "conv-2", "s2", "r2", "m105");
    }

    @AfterEach
    void cleanup() {
        mongoTemplate.dropCollection("messages");
    }

    @Test
    void retrieveHistory_filtersByConversationAndSortsDesc() {
        List<Message> results = messageRepository.retrieveHistory(
                "conv-1",
                101L,
                PageRequest.of(0, 10)
        );

        assertEquals(3, results.size());
        assertEquals(104L, results.get(0).getId());
        assertEquals(103L, results.get(1).getId());
        assertEquals(102L, results.get(2).getId());
    }

    @Test
    void retrieveHistory_respectsPageSize() {
        List<Message> results = messageRepository.retrieveHistory(
                "conv-1",
                100L,
                PageRequest.of(0, 2)
        );

        assertEquals(2, results.size());
        assertEquals(104L, results.get(0).getId());
        assertEquals(103L, results.get(1).getId());
    }

    private void insertMessage(Long id, String conversationId, String senderId, String receiverId, String content) {
        Document document = new Document()
                .append("_id", id)
                .append("conservation_id", conversationId)
                .append("sender_id", senderId)
                .append("receiver_id", receiverId)
                .append("content", content);

        mongoTemplate.getCollection("messages").insertOne(document);
    }
}
