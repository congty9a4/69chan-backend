package com.congty9a4.backend.benchmark;

public final class BenchmarkConstants {

    // UUID cố định — idempotent qua nhiều lần chạy test

    public static final String BENCHMARK_EMAIL = "benchmark@test.com";
    public static final String BENCHMARK_USERNAME = "benchmark_user";
    public static final String BENCHMARK_PASSWORD = "hashed_password_placeholder";

    // Prefix để cleanup an toàn — chỉ xóa user có prefix này
    public static final String AUTHOR_USERNAME_PREFIX = "bench_author_";

    public static final int FOLLOWING_COUNT = 20;  // số user sẽ follow
    public static final int POSTS_PER_AUTHOR = 500;  // post mỗi author
    // Tổng: 20 × 50 = 1000 posts trong feed → đủ để paginate có ý nghĩa
}
