package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    @Query(value = "{ 'participant_ids': ?0 }", sort = "{ 'last_message_time': -1 }")
    List<Conversation> findAllMyChats(String userId);
}
