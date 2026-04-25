package com.example.home_thermostat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.home_thermostat_api.enums.ThermostatModes;
import com.example.home_thermostat_api.enums.ThermostatStatuses;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.ThermostatService;
import com.example.home_thermostat_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/thermostats")
public class ThermostatController {

    @Autowired
    private ThermostatService thermostatService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get thermostat", description = "Returns thermostat details")
    @ApiResponse(responseCode = "200", description = "Thermostat found")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Thermostat> getThermostat(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(thermostatService.getById(id, user));
    }

    @Operation(summary = "Get thermostat by room", description = "Returns thermostat for a room")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Thermostat> getByRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(thermostatService.getByRoomId(roomId, user));
    }

    @Operation(summary = "Set target temperature", description = "Sets the desired temperature")
    @ApiResponse(responseCode = "200", description = "Temperature set")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/target")
    public ResponseEntity<Thermostat> setTargetTemperature(
            @PathVariable Long id,
            @RequestParam Double value,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(thermostatService.setTargetTemperature(id, value, user));
    }

    @Operation(summary = "Set mode", description = "Sets thermostat mode: HEAT, COOL, OFF")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/mode")
    public ResponseEntity<Thermostat> setMode(
            @PathVariable Long id,
            @RequestParam ThermostatModes mode,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(thermostatService.setMode(id, mode, user));
    }

    @Operation(summary = "Set status", description = "Sets thermostat status: ACTIVE, INACTIVE")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/status")
    public ResponseEntity<Thermostat> setStatus(
            @PathVariable Long id,
            @RequestParam ThermostatStatuses status,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(thermostatService.setStatus(id, status, user));
    }
}
