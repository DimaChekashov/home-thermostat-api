package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.service.RoomService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/rooms")
@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    public Room getById(@PathVariable Long id) {
        return roomService.getById(id);
    }

    // @GetMapping("/{id}/temperatures")
    // public List<TemperatureReading> getAllTemperaturesByRoomId(@PathVariable Long id) {
    //     return roomService.getAllTemperaturesByRoomId(id);
    // }

    // @PostMapping("/{id}/target")
    // public Room updateTargetTemperature(@PathVariable Long id, @RequestParam Double target) {
    //     return roomService.updateTargetTemperature(id, target);
    // }

    // @PostMapping
    // public Room create(@RequestParam Long homeId, @RequestBody Room room) {
    //     return roomService.create(homeId, room);
    // }
}
