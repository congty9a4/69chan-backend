package com.congty9a4.backend.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"local", "common"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FeedBenchmarkTest {

    // Số lần warmup — không tính vào kết quả
    // JVM cần vài lần để JIT compile → số đầu tiên thường cao bất thường
    private static final int WARMUP_RUNS = 10;
    private static final int MEASURE_RUNS = 100;
    // ─── Điều chỉnh đúng endpoint của bạn ────────────────────────────────────
    private static final String FEED_ENDPOINT = "/api/feeds";
    private static String benchmarkUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BenchmarkDataSeeder seeder;
    @Autowired
    private ObjectMapper objectMapper;

    // ─── Setup / Teardown ─────────────────────────────────────────────────────

    @BeforeAll
    static void setUp(@Autowired BenchmarkDataSeeder seeder) {
        seeder.seed();
        benchmarkUserId = seeder.getBenchmarkUser().getId().toString();
    }

    @AfterAll
    static void tearDown(@Autowired BenchmarkDataSeeder seeder) {
        seeder.cleanup(benchmarkUserId);
    }

    // ─── Test 1: Page đầu tiên (không có cursor) ─────────────────────────────

    @Test
    @Order(1)
    void benchmark_feedFirstPage() throws Exception {
        log.info("\n[Benchmark] Warming up {} runs...", WARMUP_RUNS);
        hit(WARMUP_RUNS, null);

        log.info("[Benchmark] Measuring {} runs (first page)...", MEASURE_RUNS);
        List<Long> latencies = hit(MEASURE_RUNS, null);

        printStats("First Page — no cursor", latencies);
    }

    // ─── Test 2: Page tiếp theo (có cursor) ──────────────────────────────────

    @Test
    @Order(2)
    void benchmark_feedWithCursor() throws Exception {
        String cursor = fetchNextCursor();

        if (cursor == null) {
            log.warn("[Benchmark] Không lấy được cursor — bỏ qua test này");
            log.warn("[Benchmark] Kiểm tra lại: feed có trả về page_info.next_cursor không?");
            return;
        }

        log.info("[Benchmark] Cursor lấy được: {}", cursor);
        log.info("[Benchmark] Warming up {} runs...", WARMUP_RUNS);
        hit(WARMUP_RUNS, cursor);

        log.info("[Benchmark] Measuring {} runs (with cursor)...", MEASURE_RUNS);
        List<Long> latencies = hit(MEASURE_RUNS, cursor);

        printStats("With Cursor — page 2+", latencies);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Gửi N request, trả về list latency (ms).
     * Inject userId vào SecurityContext thay vì dùng JWT thật
     * → Tránh token expiry, isolate đúng phần feed logic cần đo.
     */
    private List<Long> hit(int count, String cursor) {
        List<Long> latencies = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            var req = get(FEED_ENDPOINT)
                    .with(user(benchmarkUserId)
                            .roles("USER"));

            // Thêm cursor nếu có
            // Điều chỉnh tên param cho đúng với endpoint của bạn
            if (cursor != null) {
                req.param("cursor", cursor);
            }

            long start = System.currentTimeMillis();

            latencies.add(System.currentTimeMillis() - start);

        }

        return latencies;
    }

    /**
     * Lấy next_cursor từ response page đầu tiên.
     * Dựa theo CursorPageResponse<T> và ApiResponse<T> của bạn.
     */
    private String fetchNextCursor() throws Exception {
        MvcResult result = mockMvc.perform(
                get(FEED_ENDPOINT)
                        .with(user(benchmarkUserId)
                                .roles("USER"))
        ).andReturn();

        String body = result.getResponse().getContentAsString();

        try {

            var root = objectMapper.readTree(body);
            var nextCursor = root
                    .path("page_info")  // CursorPageResponse.pageInfo
                    .path("next_cursor");

            return nextCursor.isMissingNode() ? null : nextCursor.asText();

        } catch (Exception e) {
            log.error("[Benchmark] Parse cursor thất bại: {}", e.getMessage());
            log.error("[Benchmark] Response body: {}", body);
            return null;
        }
    }

    // ─── In kết quả ──────────────────────────────────────────────────────────

    private void printStats(String label, List<Long> latencies) {
        if (latencies.isEmpty()) return;

        Collections.sort(latencies);
        long sum = latencies.stream().mapToLong(Long::longValue).sum();

        long avg = sum / latencies.size();
        long min = latencies.get(0);
        long max = latencies.get(latencies.size() - 1);
        long p50 = percentile(latencies, 50);
        long p95 = percentile(latencies, 95); // ← dùng cái này cho CV
        long p99 = percentile(latencies, 99);

        log.info("""
                        \n╔══════════════════════════════════════════════╗
                        ║  FEED BENCHMARK: {}
                        ╠══════════════════════════════════════════════╣
                        ║  Dataset : {} authors × {} posts = {} total
                        ║  Runs    : {}
                        ╠══════════════════════════════════════════════╣
                        ║  avg     : {}ms
                        ║  min     : {}ms
                        ║  max     : {}ms
                        ║  p50     : {}ms
                        ║  p95     : {}ms
                        ║  p99     : {}ms
                        ╚══════════════════════════════════════════════╝""",
                label,
                BenchmarkConstants.FOLLOWING_COUNT,
                BenchmarkConstants.POSTS_PER_AUTHOR,
                BenchmarkConstants.FOLLOWING_COUNT * BenchmarkConstants.POSTS_PER_AUTHOR,
                latencies.size(),
                avg, min, max, p50, p95, p99
        );
    }

    private long percentile(List<Long> sorted, int pct) {
        int idx = (int) Math.ceil(pct / 100.0 * sorted.size()) - 1;
        return sorted.get(Math.max(0, Math.min(idx, sorted.size() - 1)));
    }
}