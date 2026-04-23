package com.example.home_thermostat_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegisterResponse(
        boolean success,
        String message,
        String error) {
    public static RegisterResponse ok() {
        return new RegisterResponse(true, "Registration successful", null);
    }

    public static RegisterResponse fail(String errorMessage) {
        return new RegisterResponse(false, null, errorMessage);
    }
}