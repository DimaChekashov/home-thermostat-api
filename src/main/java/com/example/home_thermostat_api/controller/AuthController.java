package com.example.home_thermostat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(
                    request.name(),
                    request.email(),
                    request.password());

            RegisterResponse response = new RegisterResponse(true, token, "Bearer", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            RegisterResponse errorResponse = new RegisterResponse(false, null, null, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.name(),
                            request.password()));

            String token = authService.login(request.name());

            LoginResponse response = new LoginResponse(true, token, "Bearer");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
