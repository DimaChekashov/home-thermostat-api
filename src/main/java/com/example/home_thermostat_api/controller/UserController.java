package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        response.put("name", principal.getName());
        response.put("message", "✅ JWT работает! Вы авторизованы в системе Home Thermostat");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/homes")
    public List<Home> getAllHomesByUserId(@PathVariable Long id) {
        return userService.getAllHomesByUserId(id);
    }

    // @PostMapping
    // public User create(@RequestBody @Valid User user) {
    // return userService.create(user);
    // }
}
