package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.reddit.RedditPost;
import com.congty9a4.backend.entity.enums.PostPrivacy;
import com.congty9a4.backend.entity.post.MediaInfo;
import com.congty9a4.backend.entity.post.Post;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Component
public class RedditPostMapper {

    public Post toPost(RedditPost redditPost) {
        if (redditPost == null) {
            return null;
        }

        Set<MediaInfo> mediaFiles = new HashSet<>();
        String imageUrl = redditPost.getUrlOverriddenByDest() != null
            ? redditPost.getUrlOverriddenByDest()
            : redditPost.getUrl();

        if (isImageUrl(imageUrl)) {
            MediaInfo media = MediaInfo.builder()
                .url(imageUrl)
                .mediaType(determineMediaType(redditPost))
                .uploadedAt(convertCreatedTime(redditPost.getCreatedUtc()))
                .build();
            mediaFiles.add(media);
        }

        Set<String> tags = new HashSet<>();
        tags.add(redditPost.getSubreddit());
        if (redditPost.getLinkFlairText() != null && !redditPost.getLinkFlairText().isEmpty()) {
            tags.add(redditPost.getLinkFlairText());
        }

        String caption = redditPost.getTitle();
        if (redditPost.getSelftext() != null && !redditPost.getSelftext().isEmpty()) {
            caption = caption + "\n\n" + redditPost.getSelftext();
        }

        return Post.builder()
            .caption(caption)
            .tags(tags)
            .mediaFiles(mediaFiles)
            .likes(new HashSet<>())
            .likeCount(redditPost.getScore() != null ? redditPost.getScore() : 0)
            .commentCount(redditPost.getNumComments() != null ? redditPost.getNumComments() : 0)
            .visibility(PostPrivacy.PUBLIC)
            .isDeleted(false)
            .createdAt(convertCreatedTime(redditPost.getCreatedUtc()))
            .updatedAt(convertCreatedTime(redditPost.getCreatedUtc()))
            .build();
    }

    private boolean isImageUrl(String url) {
        if (url == null) return false;
        String lowerUrl = url.toLowerCase();
        return lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg")
            || lowerUrl.endsWith(".png") || lowerUrl.endsWith(".gif")
            || lowerUrl.contains("i.redd.it") || lowerUrl.contains("i.imgur.com");
    }

    private String determineMediaType(RedditPost redditPost) {
        if (Boolean.TRUE.equals(redditPost.getIsVideo())) {
            return "video";

        }

        String postHint = redditPost.getPostHint();
        if (postHint != null) {
            switch (postHint) {
                case "image": return "image";
                case "video": return "video";
                case "link": return "link";
            }
        }

        String url = redditPost.getUrlOverriddenByDest() != null
            ? redditPost.getUrlOverriddenByDest()
            : redditPost.getUrl();

        if (url != null) {
            String lowerUrl = url.toLowerCase();
            if (lowerUrl.endsWith(".gif")) return "gif";
            if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".webm")) return "video";
        }

        return "image";
    }

    private OffsetDateTime convertCreatedTime(Double createdUtc) {
        if (createdUtc == null) {
            return OffsetDateTime.now(ZoneOffset.UTC);
        }
        return OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(createdUtc.longValue()),
            ZoneOffset.UTC
        );
    }
}

