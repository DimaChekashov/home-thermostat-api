package com.example.home_thermostat_api.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomRequest(
        @NotBlank(message = "Name is required") String name) {
}
