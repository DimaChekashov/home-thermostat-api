package com.example.home_thermostat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.home_thermostat_api.enums.TemperatureReadingSources;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.service.TemperatureReadingService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    @Autowired
    private TemperatureReadingService temperatureReadingService;

    @Operation(summary = "Report temperature", description = "Send temperature reading from a sensor")
    @PostMapping("/{thermostatId}/temperature")
    public ResponseEntity<TemperatureReading> reportTemperature(
            @PathVariable Long thermostatId,
            @RequestParam Double value,
            @RequestParam(defaultValue = "SENSOR") TemperatureReadingSources source) {
        return ResponseEntity.ok(temperatureReadingService.recordTemperature(thermostatId, value, source));
    }
}
