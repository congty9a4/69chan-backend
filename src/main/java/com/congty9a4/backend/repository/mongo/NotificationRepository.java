package com.congty9a4.backend.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.congty9a4.backend.entity.Notification;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(String receiverId);

    long countByReceiverIdAndIsReadFalse(String receiverId);

}
