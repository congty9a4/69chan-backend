package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PullModelService implements FanoutService {

    RelationService relService;
    PostRepository postRepository;
    PostMapper postMapper;
    UserService userService;

    @Override
    public CursorPageResponse<PostResponse> getHomeFeed(CursorPageRequest pageRequest) {

        String userId = SecurityUtils.getCurrentUserId();

        Set<String> followees = relService.retrieveFollowees(userId);
        if (followees.isEmpty()) {
            return new CursorPageResponse<>(Collections.emptyList(), null);
        }

        int limit = pageRequest.getLimit();
        List<Post> posts;
        Boolean hasNext = Boolean.TRUE;
        Boolean hasPrev = false;

        if (pageRequest.getCursor() == null) {

            posts = postRepository.findPostsByUserIdsFirstPage(followees, PageRequest.of(0, limit + 1));

            if (posts.size() > limit) {
                hasNext = true;
                posts = posts.subList(0, limit);
            }
        } else {
            if (pageRequest.getDirection() == CursorPageRequest.Direction.NEXT) {
                posts = postRepository.findPostsByUserIdsAfter(followees, pageRequest.getCursor(), PageRequest.of(0, limit + 1));
                if (posts.size() > limit) {
                    hasNext = true;
                    posts = posts.subList(0, limit);
                }
                hasPrev = true;
            } else {
                posts = postRepository.findPostsByUserIdsBefore(followees, pageRequest.getCursor(), PageRequest.of(0, limit + 1));
                if (posts.size() > limit) {
                    hasPrev = true;
                    posts = posts.subList(0, limit);
                }
                Collections.reverse(posts);
                hasNext = true;
            }
        }

        List<PostResponse> postResponses = posts.stream()
                .map(post -> {
                    PostResponse response = postMapper.toPostResponse(post, userId);
                    if (post.getUserId() != null) {
                        response.setInfochan(userService.userInfo(post.getUserId()));
                    }
                    return response;
                })
                .collect(Collectors.toList());

        String nextCursor = null;
        String prevCursor = null;

        if (!posts.isEmpty()) {
            if (hasNext) {
                nextCursor = posts.get(posts.size() - 1).getCreatedAt().toString();
            }
            if (hasPrev) {
                prevCursor = posts.get(0).getCreatedAt().toString();
            }
        }

        CursorPageResponse.PageInfo pageInfo = CursorPageResponse.PageInfo.builder()
                .nextCursor(nextCursor)
                .prevCursor(prevCursor)
                .hasNext(hasNext)
                .hasPrev(hasPrev)
                .build();

        return new CursorPageResponse<>(postResponses, pageInfo);
    }

}
