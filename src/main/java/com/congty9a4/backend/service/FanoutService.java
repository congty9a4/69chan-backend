package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;

/**
 * FanoutService is responsible for distributing a post to the followers' feeds when a new post is created.
 * This service can be implemented using various strategies, such as push-based or pull-based fanout.
 */
public interface FanoutService {
    CursorPageResponse<PostResponse> getHomeFeed(CursorPageRequest pageRequest);
}
