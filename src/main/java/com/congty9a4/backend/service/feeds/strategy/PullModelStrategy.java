package com.congty9a4.backend.service.feeds.strategy;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.service.RelationService;
import com.congty9a4.backend.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PullModelStrategy implements FeedStrategy {

    RelationService relService;
    PostRepository postRepository;

    @Override
    public List<Post> fetchPosts(CursorPageRequest<String> pageRequest) {

        String cursor = pageRequest.getCursor();
        String userId = SecurityUtils.getCurrentUserId();

        Set<String> followees = relService.retrieveFollowees(userId);
        List<Post> posts = new ArrayList<>();

        if (followees.isEmpty()) {
            return posts;
        }

        if (cursor == null) {
            posts = postRepository.getUserFeedsFirstPage(followees, pageRequest.toPageRequest());
        } else {
            posts = postRepository.getUserFeedsAfterCursor(followees, parseToUTC(cursor), pageRequest.toPageRequest());
        }

        return posts;

    }

    private Instant parseToUTC(String cursor) {
        return OffsetDateTime.parse(cursor).toInstant();
    }

}
