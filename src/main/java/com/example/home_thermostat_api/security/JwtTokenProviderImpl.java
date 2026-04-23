package com.example.home_thermostat_api.security;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.home_thermostat_api.enums.TokenType;
import com.example.home_thermostat_api.model.Token;
import com.example.home_thermostat_api.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-duration-second:300}")
    private long ACCESS_TOKEN_EXPIRATION_SECONDS;

    @Value("${jwt.refresh-token-duration-second:604800}")
    private long REFRESH_TOKEN_EXPIRATION_SECONDS;

    @Value("${JWT_ACCESS_TOKEN_DURATION_MINUTE}")
    private long accessTokenDurationMinute;

    @Value("${JWT_ACCESS_TOKEN_DURATION_SECOND}")
    private long accessTokenDurationSecond;

    @Value("${JWT_REFRESH_TOKEN_DURATION_DAY}")
    private long refreshTokenDurationDay;

    @Value("${JWT_REFRESH_TOKEN_DURATION_SECOND}")
    private long refreshTokenDurationSecond;

    public Token generateAccessToken(Map<String, Object> extraClaims, User user) {
        String username = user.getUsername();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(accessTokenDurationMinute, ChronoUnit.MINUTES);

        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expiryDate))
                .signWith(getSigningKey())
                .compact();

        return new Token(TokenType.ACCESS,
                token, expiryDate, user);
    }

    public Token generateRefreshToken(User user) {
        String username = user.getUsername();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(refreshTokenDurationDay, ChronoUnit.DAYS);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expiryDate))
                .signWith(getSigningKey())
                .compact();

        return new Token(TokenType.REFRESH, token, expiryDate, user);
    }

    public boolean validateToken(String tokenValue) {
        if (tokenValue == null) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(tokenValue);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String tokenValue) {
        return extractClaim(tokenValue, Claims::getSubject);
    }

    public LocalDateTime getExpiryDateFromToken(String tokenValue) {
        return toLocalDateTime(extractClaim(tokenValue,
                Claims::getExpiration));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        return date.toInstant().atOffset(zoneOffset).toLocalDateTime();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date toDate(LocalDateTime localDateTime) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        return Date.from(localDateTime.toInstant(zoneOffset));
    }
}
