package com.congty9a4.backend.repository;

import com.congty9a4.backend.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, UUID> {
    UUID id(UUID id);
}
