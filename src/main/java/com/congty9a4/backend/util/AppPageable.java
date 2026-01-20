package com.congty9a4.backend.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Builder
public class AppPageable {

    private Pageable pageable;

    public static AppPageable of(
            int page, int size, String sortBy, String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return AppPageable.builder().pageable(PageRequest.of(page - 1, size, sort)).build();
    }

    public String nextOrPrevPage(Page page, boolean isNext, String endpoint){

        String result = endpoint;

        if (isNext){
            if(!page.hasNext()) return null;
            result += "?page=" + (page.getNumber() + 2) + "&size=" + page.getSize();
        } else {
            if(!page.hasPrevious()) return null;
            result += "?page=" + (page.getNumber()) + "&size=" + page.getSize();
        }

        result += "&sortBy=" + pageable.getSort().toList().getFirst().getProperty();
        result += "&sortDir=" + (pageable.getSort().toList().getFirst().isAscending() ? "asc" : "desc");
        return result;
    }
}
