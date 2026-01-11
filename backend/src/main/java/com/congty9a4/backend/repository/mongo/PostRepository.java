package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PostRepository extends MongoRepository<Post, String> {
    @Query("{'user_id': ?0}")
    Page<Post> findAllByUserId(String userId, Pageable pageable);
}
