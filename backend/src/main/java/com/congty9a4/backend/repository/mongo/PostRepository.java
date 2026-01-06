package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
