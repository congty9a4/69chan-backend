package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.post.PostCreationRequest;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ApiResponse<PostResponse> createPost(
            @RequestPart("file") List<MultipartFile> mediaFiles,
            @RequestPart("post") PostCreationRequest post) {
        var result = postService.createPost(post, mediaFiles);
        return ApiResponse.success(result);
    }

 /*   @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
   }*/

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable String id) {
        var result = postService.getPostById(id);
        return ApiResponse.success(result);
    }
}

