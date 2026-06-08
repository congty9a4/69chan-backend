package com.congty9a4.backend.benchmark;

import com.congty9a4.backend.entity.Relationship;
import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.entity.enums.PostPrivacy;
import com.congty9a4.backend.entity.enums.Relation;
import com.congty9a4.backend.entity.enums.RelationEntity;
import com.congty9a4.backend.entity.post.Post;
import com.congty9a4.backend.repository.jpa.RelationshipRepository;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.repository.mongo.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BenchmarkDataSeeder {

    private final UserRepository userchanRepository;
    private final RelationshipRepository relationshipRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    // ─── Entry point ─────────────────────────────────────────────────────────

    @Transactional
    public void seed() {
        if (isAlreadySeeded()) {
            log.info("[Benchmark] Data đã tồn tại — bỏ qua seed");
            return;
        }
        log.info("[Benchmark] Bắt đầu seed...");

        Userchan benchmarkUser = seedBenchmarkUser();
        List<Userchan> authors = seedAuthors();
        seedRelationships(benchmarkUser, authors);
        seedPosts(authors);

        log.info("[Benchmark] Xong — {} authors, {} posts",
                authors.size(),
                authors.size() * BenchmarkConstants.POSTS_PER_AUTHOR
        );
    }

    // ─── Kiểm tra idempotent ──────────────────────────────────────────────────

    private boolean isAlreadySeeded() {
        return userchanRepository.existsByEmail(BenchmarkConstants.BENCHMARK_EMAIL);
    }

    // ─── Tạo benchmark user ───────────────────────────────────────────────────

    private Userchan seedBenchmarkUser() {
        Userchan user = Userchan.builder()
                //       .id(BenchmarkConstants.BENCHMARK_USER_ID)
                .username(BenchmarkConstants.BENCHMARK_USERNAME)
                .email(BenchmarkConstants.BENCHMARK_EMAIL)
                .password(passwordEncoder.encode(BenchmarkConstants.BENCHMARK_PASSWORD))
                .isActive(true)
                .isVerified(true)
                .build();


        return userchanRepository.save(user);
    }

    // Thêm method này vào seeder
    public Userchan getBenchmarkUser() {
        return userchanRepository
                .findByEmail(BenchmarkConstants.BENCHMARK_EMAIL)
                .orElseThrow(() -> new IllegalStateException(
                        "[Benchmark] Benchmark user không tồn tại — seed() chưa chạy?"
                ));
    }

    // ─── Tạo 20 author mà benchmark_user sẽ follow ───────────────────────────

    private List<Userchan> seedAuthors() {
        List<Userchan> authors = new ArrayList<>();

        for (int i = 0; i < BenchmarkConstants.FOLLOWING_COUNT; i++) {
            Userchan author = Userchan.builder()
                    .username(BenchmarkConstants.AUTHOR_USERNAME_PREFIX + i)
                    .email(BenchmarkConstants.AUTHOR_USERNAME_PREFIX + i + "@test.com")
                    .password(passwordEncoder.encode("dummypass"))
                    .isActive(true)
                    .isVerified(true)
                    .build();

            authors.add(author);
        }

        return userchanRepository.saveAll(authors);
    }

    // ─── Tạo quan hệ follow trong Relationship table ─────────────────────────
    //
    // Dựa theo entity Relationship của bạn:
    // object → relation → subject
    // benchmark_user --[FOLLOWER]--> author
    //
    // Nếu feed service query theo chiều ngược lại,
    // đổi objectId/subjectId cho phù hợp với logic thực tế.

    private void seedRelationships(Userchan follower, List<Userchan> authors) {
        List<Relationship> relationships = authors.stream()
                .map(author -> Relationship.builder()
                        .objectName(RelationEntity.user.name())
                        .objectId(follower.getId().toString())
                        .relation(Relation.FOLLOWER.name())
                        .subjectName(RelationEntity.user.name())
                        .subjectId(author.getId().toString())
                        .subjectRelation(Relation.VIEWER.name()) // điều chỉnh nếu bạn dùng khác
                        .build()
                )
                .toList();

        relationshipRepository.saveAll(relationships);
        log.info("[Benchmark] Đã tạo {} relationships", relationships.size());
    }

    // ─── Tạo posts trong MongoDB ──────────────────────────────────────────────

    private void seedPosts(List<Userchan> authors) {
        List<Post> posts = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();

        for (Userchan author : authors) {
            for (int i = 0; i < BenchmarkConstants.POSTS_PER_AUTHOR; i++) {

                // Rải posts ra 30 ngày để cursor pagination hoạt động thực tế
                // Nếu tất cả posts có cùng createdAt → cursor sẽ không ý nghĩa
                OffsetDateTime createdAt = now
                        .minusHours((long) i * 14)
                        .minusMinutes((long) (Math.random() * 60));

                Post post = Post.builder()
                        .userId(author.getId().toString())
                        .caption("Benchmark post #" + i + " by " + author.getUsername())
                        .tags(Set.of("benchmark", "test"))
                        .visibility(PostPrivacy.PUBLIC)
                        .isDeleted(false)
                        .likeCount(0)
                        .commentCount(0)
                        .createdAt(createdAt)
                        .updatedAt(createdAt)
                        .build();

                posts.add(post);
            }
        }

        // saveAll batch — nhanh hơn lặp save() từng cái
        postRepository.saveAll(posts);
        log.info("[Benchmark] Đã tạo {} posts trong MongoDB", posts.size());
    }

    // ─── Cleanup — gọi trong @AfterAll ───────────────────────────────────────

    @Transactional
    public void cleanup(String benchmarkUserId) {
        log.info("[Benchmark] Cleanup...");

        // 1. Xóa posts của các bench_author trong MongoDB
        List<String> authorIds = userchanRepository
                .findAllByUsernameStartingWith(BenchmarkConstants.AUTHOR_USERNAME_PREFIX)
                .stream()
                .map(u -> u.getId().toString())
                .toList();

        postRepository.deleteAllByUserIdIn(authorIds);

        // 2. Xóa relationships
        relationshipRepository.deleteAllByObjectId(
                benchmarkUserId
        );

        // 3. Xóa users — bench_author_* trước, sau đó benchmark_user
        userchanRepository.deleteAllByUsernameStartingWith(
                BenchmarkConstants.AUTHOR_USERNAME_PREFIX
        );
        userchanRepository.deleteById(UUID.fromString(benchmarkUserId));

        log.info("[Benchmark] Cleanup xong");
    }
}