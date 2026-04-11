package com.example.home_thermostat_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.home_thermostat_api.model.Temperature;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
}
