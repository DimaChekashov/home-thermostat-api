package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.TemperatureReadingService;
import com.example.home_thermostat_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RequestMapping("/api/thermostats/{thermostatId}/temperatures")
@RestController
public class TemperatureReadingController {
    @Autowired
    private TemperatureReadingService temperatureReadingService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get temperature readings", description = "Returns latest readings for a thermostat")
    @ApiResponse(responseCode = "200", description = "List of readings")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/readings")
    public ResponseEntity<List<TemperatureReading>> getReadings(
            @PathVariable Long thermostatId,
            @RequestParam(defaultValue = "60") int minutes,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(temperatureReadingService.getReadingsForPeriod(thermostatId, minutes, user));
    }

    @Operation(summary = "Get current temperature", description = "Returns the latest temperature reading")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public ResponseEntity<Double> getCurrentTemperature(@PathVariable Long thermostatId) {
        return ResponseEntity.ok(temperatureReadingService.getCurrentTemperature(thermostatId));
    }
}
