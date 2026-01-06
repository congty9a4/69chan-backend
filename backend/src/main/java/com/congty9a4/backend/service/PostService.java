package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.post.PostCreationRequest;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.post.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostCreationRequest post, List<MultipartFile> files);
    List<PostResponse> getAllPosts();
    PostResponse getPostById(String id);
    PostResponse updatePost(String id, Post post);
    void deletePost(String id);
}
