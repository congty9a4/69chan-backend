package com.congty9a4.backend.service;

import com.congty9a4.backend.constant.LOCALE;
import com.congty9a4.backend.constant.MEDIA;
import com.congty9a4.backend.dto.req.post.PostCreationRequest;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.enums.PostVisibility;
import com.congty9a4.backend.entity.post.Infochan;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.post.PostMedia;
import com.congty9a4.backend.exception.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.SecurityUtils;
import com.congty9a4.backend.util.ServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostMapper postMapper;

    @Autowired
    ServerUtils serverUtils;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CloudStorageService cloudStorageService;

    @Override
    public PostResponse createPost(PostCreationRequest req, List<MultipartFile> files) {
        Post postEntity = postMapper.toPost(req);
        postEntity.setVisibility(req.isPublic() ? PostVisibility.PUBLIC : PostVisibility.FRIENDS);

        // fake user
        if (!userRepository.existsById(UUID.fromString(SecurityUtils.getCurrentUserId())))
            throw new AppException(ErrorCode.USER_NOT_FOUND, "Can't create post due to user not found");
        postEntity.setUserId(SecurityUtils.getCurrentUserId());
        postEntity.setMediaFiles(convertMediaFiles(files));
        var savedPost = postRepository.save(postEntity);
        return postMapper.toPostResponse(savedPost);
    }


    @Override
    public PostResponse getPostById(String id) {
            var post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND, "Post not found with id: " + id));
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setInfochan(userInfo(post.getUserId()));
            return postResponse;
    }


    @Override
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public PageResponse<List<PostResponse>> getAllPosts(AppPageable pageable) {
        String userId = SecurityUtils.getCurrentUserId();
        var currentPage = postRepository.findAllByUserId(userId, pageable.getPageable());
        var postResponses = currentPage.getContent().stream()
                .map(postMapper::toPostResponse)
                .toList();

        return PageResponse.<List<PostResponse>>builder()
                .content(postResponses)
                .page(currentPage.getNumber() + 1)
                .size(postResponses.size())
                .totalItems(currentPage.getTotalElements())
                .totalPages(currentPage.getTotalPages())
                .next(pageable.nextOrPrevPage(currentPage, true, serverUtils.getServerUrl()))
                .prev(pageable.nextOrPrevPage(currentPage, false, serverUtils.getServerUrl()))
                .build();
    }


    private Infochan userInfo(String userId) {
       Userchan user = userRepository.findById(UUID.fromString(userId)).orElseThrow(
               () -> new AppException(ErrorCode.POST_NOT_FOUND, "User of this post not found with id: " + userId)
       );
       return userMapper.toInfochan(user);
   }

   private Set<PostMedia> convertMediaFiles(List<MultipartFile> files){
       Set<PostMedia> mediaFiles = new HashSet<>();
        for (MultipartFile file : files){
            String fileName = String.join("-", UUID.randomUUID().toString(), file.getOriginalFilename());
            String url = cloudStorageService.uploadFile(file, fileName);
            String type = MEDIA.getType(fileName);
            mediaFiles.add(PostMedia.builder()
                    .url(url)
                    .uploadedAt(LOCALE.now)
                    .mediaType(type)
                    .build());
        }
        return mediaFiles;
   }
}

