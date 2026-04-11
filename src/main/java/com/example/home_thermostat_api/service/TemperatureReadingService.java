package com.example.home_thermostat_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.repository.TemperatureReadingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TemperatureReadingService {
    private final TemperatureReadingRepository temperatureReadingRepository;

    public List<TemperatureReading> getAll() {
        return temperatureReadingRepository.findAll();
    }

    public TemperatureReading getById(Long id) {
        return temperatureReadingRepository.findById(id).orElseThrow(
                RuntimeException::new);
    }

    public TemperatureReading create(TemperatureReading temperatureReading) {
        return temperatureReadingRepository.save(temperatureReading);
    }
}
