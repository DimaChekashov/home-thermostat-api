package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.exception.ResourceNotFoundException;
import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.HomeRepository;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeRepository homeRepository;

    @Override
    public Home create(String name, String address, User user) {
        Home home = new Home();

        home.setName(name);
        home.setAddress(address);
        home.setUser(user);

        return homeRepository.save(home);
    }

    @Override
    public List<Home> getUserHomes(User user) {
        return homeRepository.findAllByUser(user);
    }

    @Override
    public Home getById(Long homeId, User user) {
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new ResourceNotFoundException("Home not found"));

        if (!home.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied!");
        }

        return home;
    }

    @Override
    public void delete(Long homeId, User user) {
        Home home = getById(homeId, user);

        homeRepository.delete(home);
    }

    @Override
    public Home update(Long homeId, String name, String address, User user) {
        Home home = getById(homeId, user);

        home.setName(name);
        home.setAddress(address);

        return homeRepository.save(home);
    }
}
