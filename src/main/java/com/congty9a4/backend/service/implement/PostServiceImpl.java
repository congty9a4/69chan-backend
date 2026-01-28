package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.constant.LOCALE;
import com.congty9a4.backend.constant.MEDIA;
import com.congty9a4.backend.dto.req.CommentRequest;
import com.congty9a4.backend.dto.req.post.PostRequest;
import com.congty9a4.backend.dto.resp.CommentResponse;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.Comment;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.enums.PostPrivacy;
import com.congty9a4.backend.dto.resp.Infochan;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.entity.post.PostMedia;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.CommentMapper;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.CommentRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.service.PostService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.PaginationHelper;
import com.congty9a4.backend.util.SecurityUtils;
import com.congty9a4.backend.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostMapper postMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    PaginationHelper paginationHelper;

    @Autowired
    ServerUtils serverUtils;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private com.congty9a4.backend.service.storage.CloudStorageService cloudStorageService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest req, List<MultipartFile> files) {
        Post postEntity = postMapper.toPost(req);
        postEntity.setVisibility(req.isPublic() ? PostPrivacy.PUBLIC : PostPrivacy.FRIENDS);
        postEntity.setLikes(new HashSet<>());
        // fake user
        if (!userRepository.existsById(UUID.fromString(SecurityUtils.getCurrentUserId())))
            throw new AppException(ErrorCode.USER_NOT_FOUND, "Can't create post due to user not found");
        postEntity.setUserId(SecurityUtils.getCurrentUserId());
        postEntity.setMediaFiles(convertMediaFiles(files));
        var savedPost = postRepository.save(postEntity);
        return postMapper.toPostResponse(savedPost, userService::userInfo);
    }


    @Override
    public PostResponse getPostById(String id) {
            var post = findPost(id);
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setInfochan(userService.userInfo(post.getUserId()));
            return postResponse;
    }


    @Transactional
    @Override
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public PageResponse<List<PostResponse>> getAllPosts(AppPageable pageable) {
        String userId = SecurityUtils.getCurrentUserId();
        var userchan = userService.getUserById(UUID.fromString(userId));
        var infochan = userMapper.toInfochan(userchan);

        var currentPage = postRepository.findAllByUserId(userId, pageable.getPageable());

        return paginationHelper.buildPageResponse(
                currentPage,
                post -> postMapper.toPostResponse(post, userId),
                postResponse -> {
                    postResponse.setInfochan(infochan);
                    return postResponse;
                },
                pageable
        );
    }

    @Transactional
    @Override
    public void handlePostLikes(String id) {
        var targetPost = findPost(id);
        var currentUser = SecurityUtils.getCurrentUserId();

        if (targetPost.getLikes() == null) targetPost.setLikes(new HashSet<>());

        // Toggle like: if user already liked, remove it; otherwise add it
        if (targetPost.getLikes().contains(currentUser)) {
            targetPost.getLikes().remove(currentUser);
        } else {
            targetPost.getLikes().add(currentUser);
        }
        targetPost.setLikeCount(targetPost.getLikes().size());

        postRepository.save(targetPost);

    }

    @Transactional
    @Override
    public void handleComment(String postId, CommentRequest request) {
        Post targetPost = findPost(postId);
        String currentUserId = SecurityUtils.getCurrentUserId();

        commentRepository.save(
                Comment.builder()
                        .userId(currentUserId)
                        .post(targetPost)
                        .childComments(new HashSet<>())
                        .text(request.getText())
                        .build());

        // init comment set if not exist
        targetPost.setCommentCount(targetPost.getCommentCount() + 1);
        postRepository.save(targetPost);
    }

    @Transactional
    @Override
    public void handleChildComment(String postId, CommentRequest request, String commentId) {

        var currentPost = findPost(postId);
        String currentUserId = SecurityUtils.getCurrentUserId();
        Comment parentComment = commentRepository.findById(commentId).orElseThrow( () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        Comment childComment = commentRepository.save(Comment.builder()
                        .userId(currentUserId)
                        .text(request.getText())
                        .childComments(new HashSet<>())
                        .post(findPost(postId))
                        .parentId(commentId)
                        .build());

        if (parentComment.getChildComments() == null) parentComment.setChildComments(new HashSet<>());
        parentComment.getChildComments().add(childComment);

        currentPost.setCommentCount(currentPost.getCommentCount() + 1);
        postRepository.save(currentPost);
        commentRepository.save(parentComment);

    }

    @Override
    public PageResponse<List<CommentResponse>> getComments(String postId, AppPageable pageable) {
        // Verify post exists
        findPost(postId);

        // Get paginated comments
        var currentPage = commentRepository.findRootCommentsByPostId(postId, pageable.getPageable());

        // Cache for user info to avoid N+1 queries
        var userInfoCache = new java.util.HashMap<String, Infochan>();

        // Map comments to responses with user info
        List<CommentResponse> commentResponses = currentPage.getContent().stream()
                .map(comment -> commentMapper.toCommentResponse(comment, userId ->
                    userInfoCache.computeIfAbsent(userId, userService::userInfo)
                ))
                .toList();

        return paginationHelper.buildPageResponse(currentPage, commentResponses, pageable);
    }

    @Transactional
    @Override
    public PostResponse updatePost(String id, PostRequest req) {
        Post post = findPost(id);
        postMapper.update(post, req);
        return postMapper.toPostResponse(postRepository.save(post));
    }

   private Post findPost(String id){
        return postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "Post not found with id: " + id));
   }

   private Set<PostMedia> convertMediaFiles(List<MultipartFile> files){
       Set<PostMedia> mediaFiles = new HashSet<>();

       if (files == null || files.isEmpty()) return mediaFiles;

       log.info("[MEDIA FILES]: ");

       for (MultipartFile file : files){
            String fileName = String.join("-", UUID.randomUUID().toString(), file.getOriginalFilename());
            String url = cloudStorageService.uploadFile(file, fileName);
            String type = MEDIA.getType(fileName);
            mediaFiles.add(PostMedia.builder()
                    .url(url)
                    .uploadedAt(LOCALE.now)
                    .mediaType(type)
                    .build());
            log.info(url);
        }
        return mediaFiles;
   }
}

