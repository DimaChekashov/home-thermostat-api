package com.example.home_thermostat_api.service;

import java.util.List;

import com.example.home_thermostat_api.enums.TemperatureReadingSources;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.User;

public interface TemperatureReadingService {
    TemperatureReading recordTemperature(Long thermostatId, Double value, TemperatureReadingSources source);

    List<TemperatureReading> getLatestReadings(Long thermostatId, User user);

    List<TemperatureReading> getReadingsForPeriod(Long thermostatId, int minutes, User user);

    Double getCurrentTemperature(Long thermostatId);
}
