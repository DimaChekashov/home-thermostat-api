package com.example.home_thermostat_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.service.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            System.out.println(request.toString());
            String token = authService.register(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getName(),
                            request.getPassword()));

            String token = authService.login(request.getName());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Wrong login or password");
            return ResponseEntity.status(401).body(error);
        }
    }
}
