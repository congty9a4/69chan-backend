package com.congty9a4.backend.service.feeds;

import com.congty9a4.backend.dto.req.CursorPageRequest;
import com.congty9a4.backend.dto.resp.CursorPageResponse;
import com.congty9a4.backend.dto.resp.PostResponse;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.PostMapper;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.service.feeds.strategy.FeedStrategy;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FeedServiceImpl implements FeedService {

    UserRepository userRepository;
    FeedStrategy feedStrategy;
    PostMapper postMapper;

    @Override
    public CursorPageResponse<PostResponse> fetchFeed(CursorPageRequest<String> pageRequest) {

        List<Post> posts = feedStrategy.fetchPosts(pageRequest);

        int limit = pageRequest.getLimit();
        boolean hasNext = posts.size() > limit;

        List<Post> resultPosts = hasNext ? posts.subList(0, limit) : posts;

        List<UUID> userIds = resultPosts.stream().map(Post::getUserId).map(UUID::fromString).toList();

        List<Userchan> users = userRepository.findAllById(userIds);

        HashMap<String, Userchan> usersInfo = (HashMap<String, Userchan>) users.stream().collect(Collectors.toMap(e -> e.getId().toString(), Function.identity()));


        List<PostResponse> postResponses = resultPosts.stream()
                .map(post -> {
                    Userchan user = usersInfo.getOrDefault(post.getUserId(), null);
                    return postMapper.toPostResponse(post, user);
                })
                .filter(e -> e.getInfochan() != null)
                .toList();

        String nextCursor = null;
        if (hasNext && !resultPosts.isEmpty()) {
            nextCursor = resultPosts.getLast().getCreatedAt().toString();
        }

        CursorPageResponse.PageInfo<String> pageInfo = CursorPageResponse.PageInfo.<String>builder()
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();

        return CursorPageResponse.<PostResponse>builder()
                .data(postResponses)
                .pageInfo(pageInfo)
                .build();
    }
}
