package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long> {
    List<Message> findBySenderIdOrReceiverId(String senderId, String receiverId);

    @Query(value = "{ 'conversation_id': ?0, '_id': { $lt: ?1 }}", sort = "{ '_id': -1 }")
    List<Message> retrieveHistory(String conversationId, Long lastMessageId, Pageable pageable);
}

