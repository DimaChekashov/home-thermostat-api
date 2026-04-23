package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + name));
    }

    public List<Home> getAllHomesByUserId(Long userId) {
        User user = getById(userId);
        return user.getHomes();
    }
}
