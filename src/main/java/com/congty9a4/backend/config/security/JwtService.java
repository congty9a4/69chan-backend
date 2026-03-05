package com.congty9a4.backend.config.security;

import com.congty9a4.backend.constant.LOCALE;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.service.redis.RedisService;
import com.google.api.client.json.webtoken.JsonWebToken;
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

    private final RedisService redisService;
    @Value("${jwt.secret}")
    String jwtSecret;

    public JwtService(RedisService redisService) {
        this.redisService = redisService;
    }

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
                .claim("is_access_token", isAccessToken)
                .id(UUID.randomUUID().toString())
                .compact();
    }

    public void validateToken(String token, boolean isAccessToken) {
        if (token == null || token.trim().isEmpty() )
            throw new AppException(ErrorCode.INVALID_TOKEN, "Token not found!");
        try {

            if (redisService.isTokenBlacklisted(extractTokenId(token)))
                throw new AppException(ErrorCode.INVALID_TOKEN, "Token has been revoked");

            Claims payload = getPayload(token);


            if (payload.getSubject().isBlank()) {
                throw new AppException(ErrorCode.INVALID_TOKEN, "Token subject is missing");
            }

            if (payload.get("is_access_token") == null || (Boolean) payload.get("is_access_token") != isAccessToken) {
                throw new AppException(ErrorCode.INVALID_TOKEN, "Token type mismatch");
            }

        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN, "Expired token");
        } catch (JwtException e) {
            log.error("Token validation error: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN, "Token is invalid");
        } catch (NullPointerException e) {
            log.error("Token parsing error: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_TOKEN, "Token credential is missing or malformed");
        }
    }

    public String extractUserId(String token){
        return getPayload(token).getSubject();
    }

    public Date extractTokenExpiration(String token) {
        return getPayload(token).getExpiration();
    }

    public String extractTokenId(String token) {
        String jid = getPayload(token).getId();
        if (jid == null) throw new AppException(ErrorCode.INVALID_TOKEN, "Token ID (JID) is missing");

        return jid;
    }

    private Claims getPayload(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }
}
