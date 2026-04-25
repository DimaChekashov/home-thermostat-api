package com.example.home_thermostat_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.enums.TemperatureReadingSources;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.TemperatureReadingRepository;
import com.example.home_thermostat_api.repository.ThermostatRepository;

@Service
public class TemperatureReadingServiceImpl implements TemperatureReadingService {

    @Autowired
    private TemperatureReadingRepository readingRepository;

    @Autowired
    private ThermostatRepository thermostatRepository;

    @Override
    public TemperatureReading recordTemperature(Long thermostatId, Double value, TemperatureReadingSources source) {
        Thermostat thermostat = thermostatRepository.findById(thermostatId)
                .orElseThrow(() -> new RuntimeException("Thermostat not found"));

        TemperatureReading reading = new TemperatureReading(value, source, thermostat);
        return readingRepository.save(reading);
    }

    @Override
    public List<TemperatureReading> getLatestReadings(Long thermostatId, User user) {
        Thermostat thermostat = thermostatRepository.findById(thermostatId)
                .orElseThrow(() -> new RuntimeException("Thermostat not found"));

        if (!thermostat.getRoom().getHome().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access to thermostat denied");
        }

        return readingRepository.findTop10ByThermostatOrderByTimestampDesc(thermostat);
    }

    @Override
    public List<TemperatureReading> getReadingsForPeriod(Long thermostatId, int minutes, User user) {
        Thermostat thermostat = thermostatRepository.findById(thermostatId)
                .orElseThrow(() -> new RuntimeException("Thermostat not found"));

        if (!thermostat.getRoom().getHome().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access to thermostat denied");
        }

        LocalDateTime after = LocalDateTime.now().minusMinutes(minutes);
        return readingRepository.findByThermostatAndTimestampAfterOrderByTimestampDesc(thermostat, after);
    }

    @Override
    public Double getCurrentTemperature(Long thermostatId) {
        return readingRepository.findTopByThermostatOrderByTimestampDesc(
                thermostatRepository.findById(thermostatId).orElseThrow())
                .map(TemperatureReading::getValue)
                .orElse(null);
    }
}
