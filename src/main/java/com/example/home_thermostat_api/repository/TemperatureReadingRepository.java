package com.example.home_thermostat_api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.Thermostat;

@Repository
public interface TemperatureReadingRepository extends JpaRepository<TemperatureReading, Long> {
    Optional<TemperatureReading> findTopByThermostatOrderByTimestampDesc(Thermostat thermostat);

    List<TemperatureReading> findTop10ByThermostatOrderByTimestampDesc(Thermostat thermostat);

    List<TemperatureReading> findByThermostatAndTimestampAfterOrderByTimestampDesc(
            Thermostat thermostat, LocalDateTime after);

    List<TemperatureReading> findByThermostatIdOrderByTimestampDesc(Long thermostatId);
}
