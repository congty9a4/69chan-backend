package com.congty9a4.backend.dto.reddit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedditPost {
    String title;
    String author;
    String subreddit;

    @JsonProperty("subreddit_name_prefixed")
    String subredditPrefixed;

    String url;

    @JsonProperty("url_overridden_by_dest")
    String urlOverriddenByDest;

    Integer score;
    Integer ups;

    @JsonProperty("num_comments")
    Integer numComments;

    String thumbnail;

    @JsonProperty("is_video")
    Boolean isVideo;

    @JsonProperty("post_hint")
    String postHint;

    String selftext;

    @JsonProperty("created_utc")
    Double createdUtc;

    @JsonProperty("link_flair_text")
    String linkFlairText;

    String permalink;

    String id;
}

