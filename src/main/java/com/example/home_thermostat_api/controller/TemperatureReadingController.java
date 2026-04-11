package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.service.TemperatureReadingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RequestMapping("/api/temperatures")
@RestController
public class TemperatureReadingController {
    private final TemperatureReadingService temperatureReadingService;

    @GetMapping
    public List<TemperatureReading> getAll() {
        return temperatureReadingService.getAll();
    }

    @GetMapping("/{id}")
    public TemperatureReading getById(@PathVariable Long id) {
        return temperatureReadingService.getById(id);
    }

    @PostMapping
    public TemperatureReading create(@RequestBody @Valid TemperatureReading temperatureReading) {
        temperatureReading.setTimestamp(LocalDateTime.now());
        temperatureReading.setId(null);
        return temperatureReadingService.create(temperatureReading);
    }
}
