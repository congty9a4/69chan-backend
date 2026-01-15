package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.req.CommentRequest;
import com.congty9a4.backend.dto.req.post.PostCreationRequest;
import com.congty9a4.backend.dto.resp.CommentResponse;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.PostService;
import com.congty9a4.backend.util.AppPageable;
import jakarta.servlet.http.HttpServletRequest;
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
            @RequestPart(value = "files", required = false) List<MultipartFile> mediaFiles,
            @RequestPart("post") PostCreationRequest post) {
        var result = postService.createPost(post, mediaFiles);
        return ApiResponse.success(result);
    }

 /*   @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
   }*/

    @GetMapping
    public ApiResponse<PageResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            HttpServletRequest request
    ) {

        var results = postService.getAllPosts(AppPageable.of(page, size, sortBy, sortDir, request.getServletPath()));
        return ApiResponse.success(results);
    }
    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable String id) {
        return ApiResponse.success(postService.getPostById(id));
    }

   /*
    @PutMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(@PathVariable String id, @RequestBody PostCreationRequest req){

    }
*/
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePost(@PathVariable String id){
        postService.deletePost(id);
        return ApiResponse.success("Post deleted successfully");
    }

    @PatchMapping("/{id}/like")
    public void handleLike(@PathVariable String id) {
        postService.handlePostLikes(id);
    }


    @GetMapping("/{postId}/comments")
    public ApiResponse<PageResponse<List<CommentResponse>>> getComments(
            @PathVariable String postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            HttpServletRequest request
    ) {
        var results = postService.getComments(postId, AppPageable.of(page, size, sortBy, sortDir, request.getServletPath()));
        return ApiResponse.success(results);
    }

    @PostMapping("/{postId}/comments")
    public void handleCommentPost(@PathVariable String postId, @RequestBody CommentRequest request){
        postService.handleComment(postId, request);
    }

    @PostMapping("/{postId}/comments/{commentId}")
    public void handleChildComment(@PathVariable String postId, @PathVariable String commentId, @RequestBody CommentRequest request){
        postService.handleChildComment(postId, request, commentId);
    }

}

