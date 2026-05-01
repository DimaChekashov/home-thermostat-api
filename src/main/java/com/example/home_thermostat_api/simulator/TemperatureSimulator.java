package com.example.home_thermostat_api.simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.home_thermostat_api.enums.TemperatureReadingSources;
import com.example.home_thermostat_api.enums.ThermostatStatuses;
import com.example.home_thermostat_api.repository.ThermostatRepository;
import com.example.home_thermostat_api.service.TemperatureReadingService;

@Component
@EnableScheduling
public class TemperatureSimulator {

    @Autowired
    private ThermostatRepository thermostatRepository;

    @Autowired
    private TemperatureReadingService temperatureReadingService;

    @Scheduled(fixedRate = 30000)
    public void simulateTemperature() {
        thermostatRepository.findAll().forEach(thermostat -> {
            try {
                if (thermostat.getStatus() == ThermostatStatuses.INACTIVE) {
                    return;
                }

                Double currentTemp = temperatureReadingService.getCurrentTemperature(thermostat.getId());

                double targetTemp = thermostat.getTargetTemperature() != null
                        ? thermostat.getTargetTemperature()
                        : 22.0;

                double baseTemp = currentTemp != null ? currentTemp : targetTemp;

                double newTemp = baseTemp + (targetTemp - baseTemp) * 0.2 + (Math.random() - 0.5) * 0.3;

                temperatureReadingService.recordTemperature(
                        thermostat.getId(),
                        Math.round(newTemp * 10.0) / 10.0,
                        TemperatureReadingSources.SIMULATOR);
            } catch (Exception e) {
                System.err.println("Error simulate for thermostate: " + thermostat.getId() + ": " + e.getMessage());
            }
        });
    }
}
