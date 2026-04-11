package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Temperature;
import com.example.home_thermostat_api.repository.TemperatureRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TemperatureService {
    private final TemperatureRepository temperatureRepository;

    public List<Temperature> getAll() {
        return temperatureRepository.findAll();
    }

    public Temperature getById(Long id) {
        return temperatureRepository.findById(id).orElseThrow(
                RuntimeException::new);
    }

    public Temperature create(Temperature temperature) {
        return temperatureRepository.save(temperature);
    }
}
