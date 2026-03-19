package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface PostRepository extends MongoRepository<Post, String>{
    @Query("{'user_id': ?0}")
    Page<Post> findAllByUserId(String userId, Pageable pageable);

    @Query("{ $text:  {$search :  ?0}}")
    List<Post> postByKeywords(String query);


    @Query(value = "{'user_id': {$in: ?0}, 'is_deleted': false}", sort = "{'created_at': -1}")
    List<Post> getUserFeedsFirstPage(Set<String> userIds, Pageable pageable);

    @Query(value = "{'user_id': {$in: ?0}, 'is_deleted': false, 'createdAt.dateTime': {$lt: ?1}}", sort = "{'created_at': -1}")
    List<Post> getUserFeedsAfterCursor(Set<String> userIds, Instant cursor, Pageable pageable);

}
