package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.exception.ResourceNotFoundException;
import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HomeService homeService;

    @Override
    public Room create(String name, Long homeId, User user) {
        Home home = homeService.getById(homeId, user);

        Room room = new Room();
        room.setName(name);
        room.setHome(home);

        return roomRepository.save(room);
    }

    @Override
    public List<Room> getRoomsByHomeId(Long homeId, User user) {
        Home home = homeService.getById(homeId, user);
        return roomRepository.findAllByHome(home);
    }

    @Override
    public Room getById(Long roomId, User user) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomId));

        if (!room.getHome().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have access to this room");
        }

        return room;
    }

    @Override
    public Room update(Long roomId, String name, User user) {
        Room room = getById(roomId, user);
        room.setName(name);

        return roomRepository.save(room);
    }

    @Override
    public void delete(Long roomId, User user) {
        Room room = getById(roomId, user);
        roomRepository.delete(room);
    }
}
