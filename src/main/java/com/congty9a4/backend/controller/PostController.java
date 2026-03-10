package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.req.post.PostRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.FanoutService;
import com.congty9a4.backend.service.PostService;
import com.congty9a4.backend.service.crawling.RedditCrawlingService;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post", description = "Post and comment management APIs")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private RedditCrawlingService redditCrawlingService;
    @Autowired
    private FanoutService fanoutService;

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    @Operation(summary = "Create post", description = "NOTE: If encounter with 500 error, ensure set Content-Type of 'files' & 'post' to 'multipart/form-data' and 'application/json' respectively \\\n'")
    public ApiResponse<PostResponse> createPost(
            @RequestPart(value = "files", required = false) List<MultipartFile> mediaFiles,
            @RequestPart("post") PostRequest post) {
        var result = postService.createPost(post, mediaFiles);
        return ApiResponse.success(result);
    }


    @GetMapping("/feeds")
    @Operation(summary = "Get home feed with cursor-based pagination")
    public ApiResponse<CursorPageResponse<PostResponse>> getHomeFeed(
            CursorPageRequest request){
        return ApiResponse.success(fanoutService.getHomeFeed(request));
    }


    @GetMapping
    @Operation(summary = "Get all posts", description = "Retrieve a paginated list of all posts")
    public ApiResponse<PageResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir
    ) {

        var results = postService.getAllPosts(AppPageable.of(page, size, sortBy, sortDir));
        return ApiResponse.success(results);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieve a specific post by its unique identifier")
    public ApiResponse<PostResponse> getPostById(@PathVariable String id) {
        return ApiResponse.success(postService.getPostById(id));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Update an existing post by ID")
    public ApiResponse<PostResponse> updatePost(@PathVariable String id, @RequestBody PostRequest req){
        return ApiResponse.success( postService.updatePost(id, req));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Delete a post by its ID")
    public ApiResponse<String> deletePost(@PathVariable String id){
        postService.deletePost(id);
        return ApiResponse.success("Post deleted successfully");
    }


}

