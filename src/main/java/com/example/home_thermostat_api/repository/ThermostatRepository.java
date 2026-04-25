package com.example.home_thermostat_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.Thermostat;

@Repository
public interface ThermostatRepository extends JpaRepository<Thermostat, Long> {
    Optional<Thermostat> findByRoom(Room room);

    Optional<Thermostat> findByRoomId(Long roomId);
}
