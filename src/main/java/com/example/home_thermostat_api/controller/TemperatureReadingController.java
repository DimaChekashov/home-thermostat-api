package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.service.TemperatureReadingService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/temperatures")
@RestController
public class TemperatureReadingController {
    private final TemperatureReadingService temperatureReadingService;

    public TemperatureReadingController(TemperatureReadingService temperatureReadingService) {
        this.temperatureReadingService = temperatureReadingService;
    }

    @GetMapping
    public List<TemperatureReading> getAll() {
        return temperatureReadingService.getAll();
    }

    @GetMapping("/room/{roomId}")
    public List<TemperatureReading> getTemperatureReadingByRoomId(@PathVariable Long roomId) {
        return temperatureReadingService.getTemperatureReadingByRoomId(roomId);
    }

    @GetMapping("/home/{homeId}")
    public List<TemperatureReading> getTemperatureReadingByHomeId(@PathVariable Long homeId) {
        return temperatureReadingService.getTemperatureReadingByHomeId(homeId);
    }

    @PostMapping
    public TemperatureReading create(@RequestParam Long roomId,
            @RequestBody @Valid TemperatureReading temperatureReading) {
        return temperatureReadingService.create(roomId, temperatureReading);
    }
}
