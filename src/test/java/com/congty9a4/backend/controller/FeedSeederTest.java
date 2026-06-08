package com.congty9a4.backend.controller;

import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class FeedSeederTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    void seedFeedData() {
        // 1. Create a guest user
        Userchan guestUser = new Userchan();
        guestUser.setUsername("guest");
        guestUser.setEmail("guest@example.com");
        guestUser.setPassword("password"); // In a real app, this would be hashed
        Userchan savedUser = userRepository.save(guestUser);

        // 2. Create posts
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Post post = new Post();
            post.setCaption("Post " + i);
            post.setUserId(savedUser.getId().toString());
            posts.add(post);
        }
        postRepository.saveAll(posts);

        // 3. Verify that the data was saved correctly
        assertEquals(1, userRepository.count());
        assertEquals(10, postRepository.count());
    }
}
