package com.congty9a4.backend.script;

import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PopulatePosts implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void run(String... args) {
        // To avoid re-populating data on every startup
        if (postRepository.count() > 0) {
            return;
        }

        List<Userchan> users = userRepository.findAll();

        if (users.isEmpty()) {
            return;
        }

        for (Userchan user : users) {
            postRepository.save(Post.builder()
                    .userId(user.getId().toString())
                    .caption("Post 1 from " + user.getUsername())
                    .build());
            postRepository.save(Post.builder()
                    .userId(user.getId().toString())
                    .caption("Post 2 from " + user.getUsername())
                    .build());
        }
    }
}

