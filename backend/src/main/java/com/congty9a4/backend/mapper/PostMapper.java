package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.req.post.PostCreationRequest;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.post.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {

    Post toPost(PostCreationRequest req);

    @Mapping(target = "scope", expression = "java(post.getVisibility().toString().toLowerCase())")
    PostResponse toPostResponse(Post post);

}
