package com.congty9a4.backend;

import com.congty9a4.backend.entity.Post;
import com.congty9a4.backend.repository.PostRepository;
import com.congty9a4.backend.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories
public class BackendApplication implements CommandLineRunner {

    @Autowired
    PostService postService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    public void run(String... args) throws Exception {
        log.info("Creating sample post...");
        postService.createPost(new Post());
    }
}
