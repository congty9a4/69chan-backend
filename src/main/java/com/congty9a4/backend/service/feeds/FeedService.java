package com.congty9a4.backend.service.feeds;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;

public interface FeedService {

    CursorPageResponse<PostResponse> fetchFeed(CursorPageRequest<String> pageRequest);
}
