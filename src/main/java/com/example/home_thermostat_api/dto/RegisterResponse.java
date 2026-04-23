package com.example.home_thermostat_api.dto;

public record RegisterResponse(
                boolean success,
                String accessToken,
                String refreshToken,
                String tokenType,
                String message) {
        public static RegisterResponse success(String accessToken, String refreshToken) {
                return new RegisterResponse(true, accessToken, refreshToken, "Bearer", "Registration successful");
        }

        public static RegisterResponse error(String message) {
                return new RegisterResponse(false, null, null, null, message);
        }
}