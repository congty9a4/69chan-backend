package com.congty9a4.backend.service.feeds.strategy;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.entity.post.Post;

import java.util.List;

/**
 * FeedStrategy is responsible for distributing a post to the followers' feeds when a new post is created.
 * This service can be implemented using various strategies, such as push-based or pull-based fanout.
 */
public interface FeedStrategy {
    List<Post> fetchPosts(CursorPageRequest<String> pageRequest);
}
