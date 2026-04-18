package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long> {
    List<Message> findBySenderIdOrReceiverId(String senderId, String receiverId);
}

