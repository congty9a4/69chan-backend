package com.congty9a4.backend.config.security;

import com.congty9a4.backend.constant.LOCALE;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Service
public class JwtService {


    @Value("${jwt.secret}")
    String jwtSecret;

    private SecretKey getSigningKey () {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Value("${jwt.expiration.access}")
    long accessExpiration;

    @Value("${jwt.expiration.refresh}")
    long refreshExpiration;

    public String createToken(String userId, boolean isAccessToken) {

        long expiration = isAccessToken ? accessExpiration : refreshExpiration;

        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(LOCALE.now.toInstant()))
                .expiration(Date.from(LOCALE.now.toInstant().plusSeconds(expiration)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .id(UUID.randomUUID().toString())
                .compact();
    }

    public void validateToken(String token) {
        if (token == null || token.trim().isEmpty() )
            throw new AppException(ErrorCode.INVALID_TOKEN, "Token not found!");

        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN, "Expired token");
        } catch (JwtException e) {
            log.error("Token validation error: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN, "Token is invalid");
        }
    }

    public String extractUserId(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
