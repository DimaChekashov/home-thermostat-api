package com.example.home_thermostat_api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${JWT_ACCESS_COOKIE_NAME}")
    private String accessTokenCookieName;

    @Value("${JWT_REFRESH_COOKIE_NAME}")
    private String refreshTokenCookieName;

    @Value("${JWT_ACCESS_TOKEN_DURATION_SECOND}")
    private long accessTokenDurationSecond;

    @Value("${JWT_REFRESH_TOKEN_DURATION_SECOND}")
    private long refreshTokenDurationSecond;

    public HttpCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(accessTokenCookieName, accessToken)
                .maxAge(accessTokenDurationSecond)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();
    }

    public HttpCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(refreshTokenCookieName, refreshToken)
                .maxAge(refreshTokenDurationSecond)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, "")
                .maxAge(0).httpOnly(true).path("/").build();
    }

    public HttpCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, "").maxAge(0).httpOnly(true).path("/").build();
    }
}