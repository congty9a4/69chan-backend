package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.nosql.Post;
import com.congty9a4.backend.repository.mongo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(String id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post updatePost(String id, Post post) {
        if (postRepository.existsById(id)) {
            post.setId(id);
            return postRepository.save(post);
        }
        return null;
    }

    @Override
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}

