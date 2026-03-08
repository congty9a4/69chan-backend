package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.repository.mongo.PostRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Pull Model (Fanout-on-Read) Implementation
 *
 * When a user requests their home feed:
 * 1. Retrieve the list of users they follow
 * 2. Query posts from all those users
 * 3. Sort and return the aggregated results
 *
 * Pros:
 * - Simple implementation
 * - No write overhead when posting
 * - Storage efficient
 *
 * Cons:
 * - Read latency increases with number of followees
 * - Higher database load on feed requests
 *
 * Best for: Users with moderate number of followees (<1000)
 */
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
    public Set<PostResponse> getHomeFeed(String userId) {
        log.debug("Fetching home feed for user: {}", userId);

        // Step 1: Retrieve the list of users that the current user is following
        Set<String> followees = relService.retrieveFollowees(userId);

        // If not following anyone, return empty feed
        if (followees.isEmpty()) {
            log.debug("User {} is not following anyone", userId);
            return Collections.emptySet();
        }

        log.debug("User {} follows {} users", userId, followees.size());

        // Step 2: Fetch all posts from the followed users
        // Posts are already sorted by created_at DESC in the repository query
        List<Post> posts = postRepository.findPostsByUserIds(followees);

        log.debug("Found {} posts for user {}'s home feed", posts.size(), userId);

        // Step 3: Convert posts to responses and enrich with user information
        Set<PostResponse> postResponses = posts.stream()
                .map(post -> {
                    // Convert post to response DTO
                    PostResponse response = postMapper.toPostResponse(post, userId);

                    // Enrich with user information (avatar, username, etc.)
                    if (post.getUserId() != null) {
                        response.setInfochan(userService.userInfo(post.getUserId()));
                    }

                    return response;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new)); // Maintain order

        return postResponses;
    }
}
