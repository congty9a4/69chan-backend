package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.CommentRequest;
import com.congty9a4.backend.dto.req.post.PostRequest;
import com.congty9a4.backend.dto.resp.CommentResponse;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.PostService;
import com.congty9a4.backend.service.crawling.RedditCrawlingService;
import com.congty9a4.backend.util.AppPageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post", description = "Post and comment management APIs")
public class InteractionController {

    @Autowired
    private PostService postService;


    @PatchMapping("/{id}/like")
    @Operation(summary = "Like/unlike post", description = "Toggle like status for a post")
    public void handleLike(@PathVariable String id) {
        postService.handlePostLikes(id);
    }


    @GetMapping("/{postId}/comments")
    @Operation(summary = "Get post comments", description = "Retrieve all comments for a specific post with pagination")
    public ApiResponse<PageResponse<List<CommentResponse>>> getComments(
            @PathVariable String postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir
    ) {
        var results = postService.getComments(postId, AppPageable.of(page, size, sortBy, sortDir));
        return ApiResponse.success(results);
    }

    @PostMapping("/{postId}/comments")
    @Operation(summary = "Add comment", description = "Add a new comment to a post")
    public void handleCommentPost(@PathVariable String postId, @RequestBody CommentRequest request){
        postService.handleComment(postId, request);
    }

    @PostMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Add reply to comment", description = "Add a child comment (reply) to an existing comment")
    public void handleChildComment(@PathVariable String postId, @PathVariable String commentId, @RequestBody CommentRequest request){
        postService.handleChildComment(postId, request, commentId);
    }

}

