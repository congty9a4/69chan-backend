package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.reddit.RedditChild;
import com.congty9a4.backend.dto.reddit.RedditPost;
import com.congty9a4.backend.dto.reddit.RedditResponse;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.RedditPostMapper;
import com.congty9a4.backend.repository.mongo.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RedditCrawlingService {

    String REDDIT_URI = "https://www.reddit.com/r";
    String[] subreddits = {"Animemes", "dankmemes", "memes"};
    String FILTER = "top.json?sort=top&t=day&limit=10";
    String SYSTEM_USER_ID = "reddit-bot";

    RedditPostMapper redditPostMapper;
    PostRepository postRepository;

    public List<Post> crawlReddit() {
        RestClient restClient = RestClient.create();
        List<Post> savedPosts = new ArrayList<>();

        for (String subreddit : subreddits) {
            String url = REDDIT_URI + "/" + subreddit + "/" + FILTER;
            log.info("Crawling URL: {}", url);

            try {
                RedditResponse response = restClient.get()
                        .uri(url)
                        .header("User-Agent", "69chan/0.1 by Chiko")
                        .retrieve()
                        .body(RedditResponse.class);

               if (response != null && response.getData() != null && response.getData().getChildren() != null) {
                    for (RedditChild child : response.getData().getChildren()) {
                        try {
                            RedditPost redditPost = child.getData();
                           if (redditPost != null) {
                                Post savedPost = redditPostMapper.toPost(redditPost);
/*                               Post savedPost = postRepository.save(post);
                                savedPosts.add(savedPost);*/

                                log.info("Saved post from r/{}: {} (ID: {})",
                                    subreddit, redditPost.getTitle(), savedPost.getId());
                            }
                        } catch (Exception e) {
                            log.error("Error processing post from r/{}: {}", subreddit, e.getMessage(), e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error crawling r/{}: {}", subreddit, e.getMessage(), e);
            }
        }

        log.info("Finished crawling Reddit. Total posts saved: {}", savedPosts.size());
        return savedPosts;
    }
}
