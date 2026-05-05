package com.congty9a4.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String OTP_PREFIX = "OTP_REGISTER:";
    private static final String COOLDOWN_PREFIX = "OTP_COOLDOWN:";
    private static final long OTP_EXPIRATION_MINUTES = 5;
    private static final long COOLDOWN_SECONDS = 60;

    public String generateAndSaveOtp(String email) {
        String cooldownKey = COOLDOWN_PREFIX + email;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
            Long expireTime = redisTemplate.getExpire(cooldownKey, TimeUnit.SECONDS);
            throw new AppException(ErrorCode.TOO_MANY_REQUESTS,
                    String.format(ErrorCode.TOO_MANY_REQUESTS.getDetailedMessage(), expireTime));
        }
        SecureRandom random = new SecureRandom();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        redisTemplate.opsForValue().set(OTP_PREFIX + email, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(cooldownKey, "locked", COOLDOWN_SECONDS, TimeUnit.SECONDS);
        return otp;
    }

    public boolean validateOtp(String email, String inputOtp) {
        String savedOtp = (String) redisTemplate.opsForValue().get(OTP_PREFIX + email);
        if (savedOtp != null && savedOtp.equals(inputOtp)) {
            redisTemplate.delete(OTP_PREFIX + email);
            redisTemplate.delete(COOLDOWN_PREFIX + email);
            return true;
        }
        return false;
    }
}
