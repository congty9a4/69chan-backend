package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.SearchResponse;
import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.SearchService;
import com.congty9a4.backend.util.AppPageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Search Controller using PostgreSQL Full Text Search (FTS)
 * Provides endpoints for searching users and posts with advanced filtering capabilities
 */
@RestController
@RequestMapping("/api/search")
@Tag(name = "Search API")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public ApiResponse<SearchResponse> search(
            @Parameter(description = "Search query (keywords or phrases)", example = "john doe")
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "posts || users", example = "posts")
            @RequestParam(value = "filter", required = false) String filter
    ) {
        var results = searchService.searchWithFilter(query, page, size, filter);
        return ApiResponse.success(results);
    }

    /*@GetMapping("/paginated")
    public ApiResponse<PageResponse<SearchResponse>> searchPaginated(
            @Parameter(description = "Search query (keywords or phrases)", example = "john doe")
            @RequestParam("query") String query,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "id")
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "desc")
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            HttpServletRequest request
    ) {
        var pageable = AppPageable.of(page, size, sortBy, sortDir, request.getServletPath());
        var results = searchService.searchWithPagination(query, pageable);
        return ApiResponse.success(results);
    }*/
}

