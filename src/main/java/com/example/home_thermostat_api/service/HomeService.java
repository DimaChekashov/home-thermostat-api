package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.repository.HomeRepository;

@Service
public class HomeService {
    private final HomeRepository homeRepository;

    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public Home getById(Long id) {
        return homeRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Room> getAllRoomsByHomeId(Long homeId) {
        Home home = getById(homeId);
        return home.getRooms();
    }

    public Home create(Home home) {
        return homeRepository.save(home);
    }

    public Home update(Long id, Home homeDetails) {
        Home home = getById(id);

        home.setName(homeDetails.getName());
        home.setAddress(homeDetails.getAddress());

        return homeRepository.save(home);
    }
}
