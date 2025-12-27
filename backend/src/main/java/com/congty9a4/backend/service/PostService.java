package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post createPost(Post post);
    List<Post> getAllPosts();
    Post getPostById(UUID id);
    Post updatePost(UUID id, Post post);
    void deletePost(UUID id);
}

