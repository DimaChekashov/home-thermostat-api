package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.Temperature;
import com.example.home_thermostat_api.service.TemperatureService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RequestMapping("/api/temperature")
@RestController
public class TemperatureController {
    private final TemperatureService temperatureService;

    @GetMapping
    public List<Temperature> getAll() {
        return temperatureService.getAll();
    }

    @GetMapping("/{id}")
    public Temperature getById(@PathVariable Long id) {
        return temperatureService.getById(id);
    }

    @PostMapping
    public Temperature create(@RequestBody @Valid Temperature temperature) {
        return temperatureService.create(temperature);
    }
}
