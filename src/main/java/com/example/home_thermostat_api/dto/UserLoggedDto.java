package com.example.home_thermostat_api.dto;

import java.util.Set;

public record UserLoggedDto(String username, String role, Set<String> permissions) {
}