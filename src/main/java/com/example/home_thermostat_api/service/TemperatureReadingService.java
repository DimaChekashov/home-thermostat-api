package com.example.home_thermostat_api.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.repository.TemperatureReadingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TemperatureReadingService {
    private final TemperatureReadingRepository temperatureReadingRepository;
    private final RoomService roomService;
    private final HomeService homeService;

    public List<TemperatureReading> getAll() {
        return temperatureReadingRepository.findAll();
    }

    public List<TemperatureReading> getTemperatureReadingByRoomId(Long roomId) {
        Room room = roomService.getById(roomId);

        return room.getTemperatureReadings();
    }

    public List<TemperatureReading> getTemperatureReadingByHomeId(Long homeId) {
        Home home = homeService.getById(homeId);
        List<Room> rooms = home.getRooms();

        if (rooms == null || rooms.isEmpty()) {
            return Collections.emptyList();
        }

        return rooms.stream()
                .filter(room -> room.getTemperatureReadings() != null)
                .flatMap(room -> room.getTemperatureReadings().stream())
                .sorted((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp()))
                .collect(Collectors.toList());
    }

    public TemperatureReading create(Long roomId, TemperatureReading temperatureReading) {
        Room room = roomService.getById(roomId);

        temperatureReading.setRoom(room);
        temperatureReading.setTimestamp(LocalDateTime.now());

        return temperatureReadingRepository.save(temperatureReading);
    }
}
