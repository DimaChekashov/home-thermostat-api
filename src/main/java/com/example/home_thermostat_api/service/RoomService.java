package com.example.home_thermostat_api.service;

import java.util.List;

import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.User;

public interface RoomService {
    Room create(String name, Long homeId, User user);

    List<Room> getRoomsByHomeId(Long homeId, User user);

    Room getById(Long roomId, User user);

    void delete(Long roomId, User user);

    Room update(Long roomId, String name, User user);
}
