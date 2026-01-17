package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.req.post.PostRequest;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.post.Infochan;
import com.congty9a4.backend.entity.post.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.function.Function;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {

    Post toPost(PostRequest req);

    @Mapping(target = "scope", expression = "java(post.getVisibility().toString().toLowerCase())")
    @Mapping(target = "isLiked", expression = "java(post.getLikes() != null && userId != null && post.getLikes().contains(userId))")
    PostResponse toPostResponse(Post post, String userId);

    default PostResponse toPostResponse(Post post) {
        return toPostResponse(post, (String) null);
    }

    default PostResponse toPostResponse(Post post, Function<String, Infochan> userInfoFetcher){
        PostResponse response = toPostResponse(post);

        if (post.getUserId() != null && userInfoFetcher != null)
            response.setInfochan(userInfoFetcher.apply(post.getUserId()));

        return response;

    }

    void update(@MappingTarget Post post, PostRequest req);

}
