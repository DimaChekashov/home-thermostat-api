package com.example.home_thermostat_api.security;

import java.time.LocalDateTime;
import java.util.Map;

import com.example.home_thermostat_api.model.Token;
import com.example.home_thermostat_api.model.User;

public interface JwtTokenProvider {
    Token generateAccessToken(Map<String, Object> extraClaims, User user);

    Token generateRefreshToken(User user);

    boolean validateToken(String tokenValue);

    String getUsernameFromToken(String tokenValue);

    LocalDateTime getExpiryDateFromToken(String tokenValue);
}
