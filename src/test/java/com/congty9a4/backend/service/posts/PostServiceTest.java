package com.congty9a4.backend.service.posts;

import com.congty9a4.backend.dto.req.post.PostRequest;
import com.congty9a4.backend.dto.resp.CommentResponse;
import com.congty9a4.backend.entity.Comment;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.post.MediaInfo;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.mapper.CommentMapper;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.CommentRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.service.NotificationService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.service.implement.PostServiceImpl;
import com.congty9a4.backend.service.storage.CloudStorageService;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.PaginationHelper;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static lombok.AccessLevel.PRIVATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Post Service Test")
@FieldDefaults(level = PRIVATE)
class PostServiceTest {

    @Mock
    PostMapper postMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentMapper commentMapper;

    @Mock
    PaginationHelper paginationHelper;

    @Mock
    UserMapper userMapper;

    @Mock
    CloudStorageService cloudStorageService;

    @Mock
    UserService userService;

    @Mock
    NotificationService notiService;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void createPost_whenUserNotExisted_shouldThrowException() {

        MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class);

        PostRequest postReq = PostRequest.builder()
                .caption("d")
                .isPublic(true)
                .build();

        String mockUserId = UUID.randomUUID().toString();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(mockUserId);

        when(postMapper.toPost(any(PostRequest.class))).thenReturn(Post.builder().build());

        when(userRepository.existsById(UUID.fromString(SecurityUtils.getCurrentUserId()))).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> postService.createPost(postReq, null));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());

        securityUtils.close();

    }

    @Test
    void likeAndUnlikePostToggle_shouldWorkWell() {

        Post post1 = Post.builder()
                .id("postId")
                .likes(new java.util.HashSet<>())
                .build();

        String currentUserId = UUID.randomUUID().toString();

        Set<String> likes = new HashSet<>();
        likes.add(currentUserId);

        Post post2 = Post.builder()
                .id("postId2")
                .likes(likes)
                .build();

        try (MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class)) {

            securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(currentUserId);
            when(postRepository.findById("postId")).thenReturn(java.util.Optional.of(post1));
            when(postRepository.findById("postId2")).thenReturn(java.util.Optional.of(post2));
            when(postRepository.save(post1)).thenReturn(post1);
            when(postRepository.save(post2)).thenReturn(post2);

            postService.handlePostLikes("postId");
            postService.handlePostLikes("postId2");

            assertEquals(1, post1.getLikes().size());
            assertEquals(0, post2.getLikes().size());
            assertTrue(post1.getLikes().contains(currentUserId));
        }

    }

    @Test
    void deletePost_shouldDeleteMediaOnCloud() {
        // Arrange
        String postId = "postId";
        MediaInfo media1 = MediaInfo.builder().id("media1").build();
        MediaInfo media2 = MediaInfo.builder().id("media2").build();
        Set<MediaInfo> mediaFiles = new HashSet<>(Arrays.asList(media1, media2));

        Post post = Post.builder()
                .id(postId)
                .mediaFiles(mediaFiles)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.deletePost(postId);

        // Assert
        verify(cloudStorageService, times(1)).deleteFile("media1");
        verify(cloudStorageService, times(1)).deleteFile("media2");
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void getComments_shouldUseCacheToAvoidNPlus1() {
        // Arrange
        String postId = "postId";
        String userId1 = "user1";
        String userId2 = "user2";

        Comment comment1 = Comment.builder().userId(userId1).build();
        Comment comment2 = Comment.builder().userId(userId1).build(); // Same user
        Comment comment3 = Comment.builder().userId(userId2).build();

        List<Comment> comments = Arrays.asList(comment1, comment2, comment3);
        Page<Comment> commentPage = new PageImpl<>(comments);

        AppPageable pageable = AppPageable.of(1, 10, "id", "asc");

        when(postRepository.findById(postId)).thenReturn(Optional.of(Post.builder().id(postId).build()));
        when(commentRepository.findRootCommentsByPostId(eq(postId), any())).thenReturn(commentPage);

        // Mock userInfo calls - should only be called once per unique user
        when(userService.userInfo(userId1)).thenReturn(Infochan.builder().userId(userId1).build());
        when(userService.userInfo(userId2)).thenReturn(Infochan.builder().userId(userId2).build());

        // Mock commentMapper to call real default method so lambda is executed
        when(commentMapper.toCommentResponse(any(Comment.class))).thenReturn(CommentResponse.builder().build());
        when(commentMapper.toCommentResponse(any(Comment.class), any())).thenCallRealMethod();

        // Act
        postService.getComments(postId, pageable);

        // Assert
        verify(userService, times(1)).userInfo(userId1);
        verify(userService, times(1)).userInfo(userId2);
        verify(commentRepository, times(1)).findRootCommentsByPostId(eq(postId), any());
    }

}
