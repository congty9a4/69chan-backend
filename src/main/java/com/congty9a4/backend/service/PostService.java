package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.CommentRequest;
import com.congty9a4.backend.dto.req.post.PostRequest;
import com.congty9a4.backend.dto.resp.CommentResponse;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.util.AppPageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest post, List<MultipartFile> files);
    PostResponse getPostById(String id);
    void deletePost(String id);
    PageResponse<List<PostResponse>> getAllPosts(AppPageable of);

    void handlePostLikes(String id);

    void handleComment(String postId, CommentRequest request);

    void handleChildComment(String postId, CommentRequest request, String commentId);

    PageResponse<List<CommentResponse>> getComments(String postId, AppPageable pageable);

    PostResponse updatePost(String id, PostRequest req);

    PageResponse<List<PostResponse>> getFeed(AppPageable pageable);
}
