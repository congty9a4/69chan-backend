package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.nosql.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post);
    List<Post> getAllPosts();
    Post getPostById(String id);
    Post updatePost(String id, Post post);
    void deletePost(String id);
}

