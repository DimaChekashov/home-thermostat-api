package com.example.home_thermostat_api.dto;

public record LoginResponse(
                boolean isLogged,
                String roles,
                String message) {
        public static LoginResponse success(String roles) {
                return new LoginResponse(true, roles, "Login successful");
        }

        public static LoginResponse error(String message) {
                return new LoginResponse(false, null, message);
        }
}
