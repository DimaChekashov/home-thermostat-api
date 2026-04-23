package com.example.home_thermostat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.RegisterResponse;
import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.dto.LoginResponse;
import com.example.home_thermostat_api.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "API для аутентификации и управления сессиями")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "User Registration", description = "Registers a new user account. Returns JWT tokens in HttpOnly cookies.")
    @ApiResponse(responseCode = "201", description = "User successfully registered")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Username or email already exists")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "User Login", description = "Authenticates a user by username and password. Returns JWT tokens.")
    @ApiResponse(responseCode = "200", description = "Successful authentication")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "Refresh Token", description = "Generates a new access token using a valid refresh token.")
    @ApiResponse(responseCode = "200", description = "Token successfully refreshed")
    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    @ApiResponse(responseCode = "404", description = "Refresh token not provided")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.notFound().build();
        }
        return authService.refresh(refreshToken);
    }

    @Operation(summary = "Logout", description = "Invalidates current JWT tokens and clears cookies.")
    @ApiResponse(responseCode = "200", description = "Successfully logged out")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return authService.logout(accessToken, refreshToken);
    }
}
