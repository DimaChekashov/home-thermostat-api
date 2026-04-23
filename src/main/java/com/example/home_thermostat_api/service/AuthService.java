package com.example.home_thermostat_api.service;

import org.springframework.http.ResponseEntity;

import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.dto.LoginResponse;
import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.RegisterResponse;

public interface AuthService {
    ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest);

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

    ResponseEntity<LoginResponse> refresh(String refreshToken);

    ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken);
}