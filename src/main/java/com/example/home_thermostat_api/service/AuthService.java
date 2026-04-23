package com.example.home_thermostat_api.service;

import org.springframework.http.ResponseEntity;

import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.dto.LoginResponse;
import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.RegisterResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest, HttpServletResponse response);

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, HttpServletResponse response);

    ResponseEntity<LoginResponse> refresh(String refreshToken, HttpServletResponse response);

    ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken, HttpServletResponse response);
}