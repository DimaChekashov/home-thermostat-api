package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.service.HomeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/homes")
@RestController
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/{id}")
    public Home getById(@PathVariable Long id) {
        return homeService.getById(id);
    }

    @PostMapping
    public Home create(@RequestBody @Valid Home home) {
        return homeService.create(home);
    }

    @PostMapping("/{id}")
    public Home update(@PathVariable Long id, @RequestBody Home homeDetails) {
        return homeService.update(id, homeDetails);
    }

    @GetMapping("/{id}/rooms")
    public List<Room> getAllRoomsByHomeId(@PathVariable Long id) {
        return homeService.getAllRoomsByHomeId(id);
    }

}
