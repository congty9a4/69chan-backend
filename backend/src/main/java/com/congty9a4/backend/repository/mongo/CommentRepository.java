package com.congty9a4.backend.repository.mongo;

import com.congty9a4.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Set;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Set<Comment> findRootCommentsByPostId(String postId);

    @Query("{ 'post': ObjectId(?0), 'parent_id':  null}")
    Page<Comment> findRootCommentsByPostId(String postId, Pageable pageable);
}
