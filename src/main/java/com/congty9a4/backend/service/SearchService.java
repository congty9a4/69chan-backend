package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.SearchResponse;
import com.congty9a4.backend.util.AppPageable;

public interface SearchService {
    SearchResponse searchWithFilter(String query, int page, int size, String filter);

    PageResponse<SearchResponse> searchWithPagination(String query, AppPageable pageable);
}
