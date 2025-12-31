package com.congty9a4.backend.repository;

import com.congty9a4.backend.config.mongodb.MongoConfig;
import com.congty9a4.backend.entity.relational.Userchan;
import com.congty9a4.backend.entity.enums.PostVisibility;
import com.congty9a4.backend.entity.nosql.Post;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.util.JsonLogging;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataMongoTest
@Import(MongoConfig.class)
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    private Post samplePost;

    @BeforeEach
    public void setUp() {

        Userchan userchan = Userchan.builder()
                .id(UUID.randomUUID())
                .username("postUser")
                .password("postPassword")
                .email("postEmail")
                .isActive(true)
                .build();

        samplePost = Post.builder()
                .visibility(PostVisibility.PUBLIC)
                .content("This is a sample post content.")
                .authorId(userchan.getId())
                .build();
        samplePost = postRepository.save(samplePost);
    }
    @AfterEach
    public void tearDown() {
        postRepository.delete(samplePost);
    }

    @Test
    void givenPost_whenSaved_thenCanBeFoundById() {
        Post savedPost = postRepository.findById(samplePost.getId()).orElse(null);

        log.info("Saved Post: {}", JsonLogging.toString(savedPost));

        assertNotNull(savedPost);
        assert savedPost.getId().equals(samplePost.getId());
        assert savedPost.getContent().equals(samplePost.getContent());
    }
}
