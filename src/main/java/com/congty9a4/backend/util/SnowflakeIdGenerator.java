package com.congty9a4.backend.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {

    private final Snowflake snowflake;

    public SnowflakeIdGenerator(
        // these parameters are not necessary now
            @Value("${snowflake.worker-id:1}") long workerId,
            @Value("${snowflake.datacenter-id:1}") long datacenterId) {
        // Defaults keep IDs stable across restarts when no explicit values are provided.
        this.snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    public long nextId() {
        return snowflake.nextId();
    }

    public String nextIdStr() {
        return snowflake.nextIdStr();
    }
}
