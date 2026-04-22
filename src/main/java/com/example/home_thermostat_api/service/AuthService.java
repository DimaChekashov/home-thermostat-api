package com.example.home_thermostat_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.UserRepository;
import com.example.home_thermostat_api.security.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(String name, String email, String password) {
        if (userRepository.existsByName(name)) {
            throw new RuntimeException("Login '" + name + "' is already exist");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email '" + email + "' is already use");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);
        System.out.println("User saved with ID: " + savedUser.getId());

        return jwtUtil.generateToken(savedUser.getName());
    }

    public String login(String name) {
        return jwtUtil.generateToken(name);
    }

}
