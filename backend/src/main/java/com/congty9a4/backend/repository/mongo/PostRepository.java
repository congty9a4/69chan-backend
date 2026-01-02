package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.nosql.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface PostRepository extends MongoRepository<Post, String> {
}
