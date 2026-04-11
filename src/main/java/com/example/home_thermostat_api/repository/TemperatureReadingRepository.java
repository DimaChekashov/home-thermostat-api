package com.example.home_thermostat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.home_thermostat_api.model.TemperatureReading;

@Repository
public interface TemperatureReadingRepository extends JpaRepository<TemperatureReading, Long> {
}
