package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.repository.RoomRepository;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HomeService homeService;

    public RoomService(RoomRepository roomRepository, HomeService homeService) {
        this.roomRepository = roomRepository;
        this.homeService = homeService;
    }

    public Room getById(Long id) {
        return roomRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    // public List<TemperatureReading> getAllTemperaturesByRoomId(Long roomId) {
    //     Room room = getById(roomId);
    //     return room.getTemperatureReadings();
    // }

    // public Room create(Long homeId, Room room) {
    //     Home home = homeService.getById(homeId);
    //     room.setHome(home);
    //     return roomRepository.save(room);
    // }

    // public Room updateTargetTemperature(Long id, Double target) {
    //     Room room = getById(id);

    //     room.setTargetTemperature(target);

    //     return roomRepository.save(room);
    // }
}
