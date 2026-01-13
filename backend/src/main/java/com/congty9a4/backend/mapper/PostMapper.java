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
    @Mapping(target = "likes", expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)")
    @Mapping(target = "isLiked", expression = "java(post.getLikes() != null && userId != null && post.getLikes().contains(userId))")
    PostResponse toPostResponse(Post post, String userId);

    default PostResponse toPostResponse(Post post) {
        return toPostResponse(post, null);
    }

}
