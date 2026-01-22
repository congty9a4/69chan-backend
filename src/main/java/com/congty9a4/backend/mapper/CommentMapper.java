package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.resp.CommentResponse;
import com.congty9a4.backend.entity.Comment;
import com.congty9a4.backend.dto.resp.Infochan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CommentMapper {

    @Mapping(target = "postId", expression = "java(comment.getPost() != null ? comment.getPost().getId() : null)")
    @Mapping(target = "infochan", ignore = true)
    @Mapping(target = "childComments", ignore = true)
    CommentResponse toCommentResponse(Comment comment);

    default CommentResponse toCommentResponse(Comment comment, Function<String, Infochan> userInfoFetcher) {
        CommentResponse response = toCommentResponse(comment);

        // Set user info for this comment
        if (comment.getUserId() != null && userInfoFetcher != null) {
            response.setInfochan(userInfoFetcher.apply(comment.getUserId()));
        }

        // Recursively set user info for child comments
        if (comment.getChildComments() != null && !comment.getChildComments().isEmpty()) {
            List<CommentResponse> children = comment.getChildComments().stream()
                    .map(child -> toCommentResponse(child, userInfoFetcher))
                    .collect(Collectors.toList());
            response.setChildComments(children);
        }

        return response;
    }

    default List<CommentResponse> toCommentResponseList(Set<Comment> comments, Function<String, Infochan> userInfoFetcher) {
        if (comments == null || comments.isEmpty()) {
            return null;
        }
        return comments.stream()
                .map(comment -> toCommentResponse(comment, userInfoFetcher))
                .collect(Collectors.toList());
    }
}

