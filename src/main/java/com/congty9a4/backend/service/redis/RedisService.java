package com.congty9a4.backend.service.redis;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RedisService {
    RedisTemplate<String, Object> redisTemplate;

    public void blacklistToken(String token, long expiration) {
        redisTemplate.opsForValue().set(token, "revoked", expiration, TimeUnit.MILLISECONDS);
    }
    public boolean isTokenBlacklisted(String jid) {
        return redisTemplate.hasKey(jid);
    }
}
