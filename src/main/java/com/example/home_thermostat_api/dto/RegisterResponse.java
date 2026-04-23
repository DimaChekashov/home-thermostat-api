package com.example.home_thermostat_api.dto;

public record RegisterResponse(
        boolean success,
        String token,
        String type,
        String error) {
}