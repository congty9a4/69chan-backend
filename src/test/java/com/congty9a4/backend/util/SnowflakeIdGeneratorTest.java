package com.congty9a4.backend.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnowflakeIdGeneratorTest {

    @Test
    void generatesIncreasingIds() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        long first = generator.nextId();
        long second = generator.nextId();

        assertThat(second).isGreaterThan(first);
    }

    @Test
    void generatesStringIds() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        String id = generator.nextIdStr();

        assertThat(id).isNotEmpty();
        assertThat(Long.parseLong(id)).isPositive();
    }
}

