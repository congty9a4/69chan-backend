package com.congty9a4.backend.repository;

import com.congty9a4.backend.config.mongodb.MongoConfig;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.enums.PostPrivacy;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.mapper.UserMapper;
import com.congty9a4.backend.repository.mongo.PostRepository;
import com.congty9a4.backend.util.JsonLogging;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataMongoTest
@Import(MongoConfig.class)
@ActiveProfiles("test")
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    private Post samplePost;

    @MockitoBean
    UserMapper userMapper;

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
                .visibility(PostPrivacy.PUBLIC)
                .caption("This is a sample post content.")
                .id(userchan.getId().toString())
                .build();
        postRepository.save(samplePost);
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
        assertEquals(savedPost.getId(), samplePost.getId());
        assertEquals(savedPost.getUserId(), samplePost.getUserId());
    }
}
