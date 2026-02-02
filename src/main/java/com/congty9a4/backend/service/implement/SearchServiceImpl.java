package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.annotation.TrackExecutionTime;
import com.congty9a4.backend.dto.resp.PageResponse;
import com.congty9a4.backend.dto.resp.SearchResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.service.SearchService;
import com.congty9a4.backend.repository.specification.UserSpecification;
import com.congty9a4.backend.util.AppPageable;
import com.congty9a4.backend.util.ServerUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SearchService implementation using JPA Specification API
 * Provides type-safe, composable full text search capabilities
 */
@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SearchServiceImpl implements SearchService {

    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;
    UserMapper userMapper;
    ServerUtils serverUtils;

    @Override
    @TrackExecutionTime
    public SearchResponse searchWithFilter(String query, AppPageable pageable, String filter) {
        log.info("Searching with query: '{}' using Specification API (filter: {})", query, filter);

        List<Userchan> users = List.of();
        List<Post> posts = List.of();

        // Build specification based on filter type
        if (filter == null) {
            // Search both users and posts
            Specification<Userchan> spec = UserSpecification.searchByFTS(query);
            users = userRepository.findAll(spec, pageable.getPageable()).getContent();
            posts = postRepository.postByKeywords(query);
            log.info("Standard FTS search returned {} users and {} posts", users.size(), posts.size());

        } else if (filter.equalsIgnoreCase("posts")) {
            // Search posts only
            posts = postRepository.postByKeywords(query);
            log.info("Posts search returned {} posts", posts.size());

        } else {
            // Default: search users only
            Specification<Userchan> spec = UserSpecification.searchByFTS(query)
                    .and(UserSpecification.orderByRelevance(query));
            users = userRepository.findAll(spec);
            log.info("User search returned {} users", users.size());
        }

        return SearchResponse.builder()
                .users(users.stream().map(userMapper::toUserResponse).toList())
                .posts(posts.stream().map(postMapper::toPostResponse).toList())
                .build();
    }

    @Override
    @TrackExecutionTime
    public PageResponse<SearchResponse> searchWithPagination(String query, AppPageable pageable) {
        log.info("Paginated search with query: '{}' using Specification API", query);

        // Build specification for FTS search with relevance ordering
        Specification<Userchan> spec = UserSpecification.searchByFTS(query)
                .and(UserSpecification.orderByRelevance(query));

        // Execute paginated search
        Page<Userchan> userPage = userRepository.findAll(spec, pageable.getPageable());
        log.info("Paginated FTS search returned {} users on page {}",
                userPage.getContent().size(), userPage.getNumber());

        // Search posts using MongoDB text search
        var posts = postRepository.postByKeywords(query);

        SearchResponse searchContent = SearchResponse.builder()
                .users(userPage.getContent().stream().map(userMapper::toUserResponse).toList())
                .posts(posts.stream().map(postMapper::toPostResponse).toList())
                .build();

        // Build paginated response
        return PageResponse.<SearchResponse>builder()
                .content(searchContent)
                .page(userPage.getNumber() + 1)
                .size(userPage.getContent().size())
                .totalItems(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .next(pageable.nextOrPrevPage(userPage, true, serverUtils.getServerUrl()))
                .prev(pageable.nextOrPrevPage(userPage, false, serverUtils.getServerUrl()))
                .build();
    }

}

