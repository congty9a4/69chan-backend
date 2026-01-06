package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.post.PostCreationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.util.AppPageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostCreationRequest post, List<MultipartFile> files);
    PostResponse getPostById(String id);
    void deletePost(String id);
    PageResponse<List<PostResponse>> getAllPosts(AppPageable of);
}
