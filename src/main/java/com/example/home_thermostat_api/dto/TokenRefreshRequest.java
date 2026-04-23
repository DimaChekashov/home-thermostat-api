package com.example.home_thermostat_api.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank(message = "Refresh token is required") String refreshToken) {
}
