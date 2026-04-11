package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/homes")
    public List<Home> getAllHomesByUserId(@PathVariable Long id) {
        return userService.getAllHomesByUserId(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }
}
