package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostRepository extends MongoRepository<Post, String>{
    @Query("{'user_id': ?0}")
    Page<Post> findAllByUserId(String userId, Pageable pageable);

    @Query("{ $text:  {$search :  ?0}}")
    List<Post> postByKeywords(String query);

    /**
     * Find all posts from a set of user IDs (for home feed)
     * Ordered by creation date descending (newest first)
     * Only returns non-deleted posts
     */
    @Query(value = "{'user_id': {$in: ?0}, 'is_deleted': false}", sort = "{'created_at': -1}")
    List<Post> findPostsByUserIds(Set<String> userIds);

}
