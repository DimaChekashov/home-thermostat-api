package com.example.home_thermostat_api.dto;

public record LoginResponse(boolean isLogged,
        String token,
        String type) {

}
