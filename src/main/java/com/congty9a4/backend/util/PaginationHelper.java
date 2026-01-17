package com.congty9a4.backend.util;

import com.congty9a4.backend.dto.resp.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PaginationHelper {

    private final ServerUtils serverUtils;

    public PaginationHelper(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * Build a paginated response from a Spring Data Page
     *
     * @param page      Spring Data Page object
     * @param mapper    Function to map entity to DTO
     * @param pageable  AppPageable with pagination info
     * @param <T>       Entity type
     * @param <R>       Response DTO type
     * @return PageResponse with mapped content
     */
    public <T, R> PageResponse<List<R>> buildPageResponse(
            Page<T> page,
            Function<T, R> mapper,
            AppPageable pageable
    ) {
        List<R> content = page.getContent().stream()
                .map(mapper)
                .toList();

        return buildPageResponse(page, content, pageable);
    }

    /**
     * Build a paginated response with custom content processing
     *
     * @param page      Spring Data Page object
     * @param content   Already processed/mapped content list
     * @param pageable  AppPageable with pagination info
     * @param <R>       Response DTO type
     * @return PageResponse with provided content
     */
    public <R> PageResponse<List<R>> buildPageResponse(
            Page<?> page,
            List<R> content,
            AppPageable pageable
    ) {
        return PageResponse.<List<R>>builder()
                .content(content)
                .page(page.getNumber() + 1)
                .size(content.size())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .next(pageable.nextOrPrevPage(page, true, serverUtils.getServerUrl()))
                .prev(pageable.nextOrPrevPage(page, false, serverUtils.getServerUrl()))
                .build();
    }

    /**
     * Build a paginated response with post-mapping processing
     *
     * @param page           Spring Data Page object
     * @param mapper         Function to map entity to DTO
     * @param postProcessor  Function to process each mapped DTO (e.g., set additional fields)
     * @param pageable       AppPageable with pagination info
     * @param <T>            Entity type
     * @param <R>            Response DTO type
     * @return PageResponse with mapped and processed content
     */
    public <T, R> PageResponse<List<R>> buildPageResponse(
            Page<T> page,
            Function<T, R> mapper,
            Function<R, R> postProcessor,
            AppPageable pageable
    ) {
        List<R> content = page.getContent().stream()
                .map(mapper)
                .map(postProcessor)
                .toList();

        return buildPageResponse(page, content, pageable);
    }
}

