package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.service.FanoutService;
import com.congty9a4.backend.service.RelationService;
import com.congty9a4.backend.service.UserService;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.Instant;
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


        int limit = pageRequest.getLimit();
        List<Post> posts;

        String userId = SecurityUtils.getCurrentUserId();

        Set<String> followees = relService.retrieveFollowees(userId);
        if (followees.isEmpty()) {
            return new CursorPageResponse<>(Collections.emptyList(), null);
        }


        if (pageRequest.getCursor() == null) {
            posts = postRepository.getUserFeedsFirstPage(followees, PageRequest.of(0, limit + 1));
        } else {
            posts = postRepository.getUserFeedsAfterCursor(followees, parseToUTC(pageRequest.getCursor()), PageRequest.of(0, limit + 1));

        }

        if (posts.isEmpty())
            return new CursorPageResponse<>(Collections.emptyList(), null);

        boolean hasNext = trimPosts(posts, limit);

        CursorPageResponse.PageInfo pageInfo = CursorPageResponse.PageInfo.builder()
                .hasNext(hasNext)
                .nextCursor(hasNext ? posts.getLast().getCreatedAt().toString() : null)
                .build();


        List<PostResponse> postResponses = posts.stream()
                .map(post -> {
                    PostResponse response = postMapper.toPostResponse(post, userId);
                    if (post.getUserId() != null) {
                        response.setInfochan(userService.userInfo(post.getUserId()));
                    }
                    return response;
                })
                .collect(Collectors.toList());

        return CursorPageResponse.<PostResponse>builder()
                    .data(postResponses)
                    .pageInfo(pageInfo)
                    .build();
    }

    private Instant parseToUTC(String cursor) {
        return OffsetDateTime.parse(cursor).toInstant();
    }

    private boolean trimPosts(List<Post> posts, int limit) {
        if (posts.size() > limit) {
            posts.subList(limit+1, posts.size()).clear();
            return true;
        }
        return false;
    }

}
