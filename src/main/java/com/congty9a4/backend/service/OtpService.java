package com.congty9a4.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String OTP_PREFIX = "OTP_REGISTER:";
    private static final long OTP_EXPIRATION_MINUTES = 5;

    public String generateAndSaveOtp(String email) {
        SecureRandom random = new SecureRandom();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        redisTemplate.opsForValue().set(OTP_PREFIX + email, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOtp(String email, String inputOtp) {
        String savedOtp = (String) redisTemplate.opsForValue().get(OTP_PREFIX + email);
        if (savedOtp != null && savedOtp.equals(inputOtp)) {
            redisTemplate.delete(OTP_PREFIX + email);
            return true;
        }
        return false;
    }
}
