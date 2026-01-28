package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.SearchResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.util.AppPageable;

import java.util.List;

public interface SearchService {

    PageResponse<SearchResponse> searchWithPagination(String query, AppPageable pageable);

    SearchResponse searchWithFilter(String query, AppPageable pageable, String filter);
}
