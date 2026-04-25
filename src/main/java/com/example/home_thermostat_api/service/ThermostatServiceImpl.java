package com.example.home_thermostat_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.enums.ThermostatModes;
import com.example.home_thermostat_api.enums.ThermostatStatuses;
import com.example.home_thermostat_api.exception.ResourceNotFoundException;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.ThermostatRepository;

@Service
public class ThermostatServiceImpl implements ThermostatService {
    @Autowired
    private ThermostatRepository thermostatRepository;

    @Override
    public Thermostat create(Room room) {
        Thermostat thermostat = new Thermostat();
        thermostat.setRoom(room);

        return thermostatRepository.save(thermostat);
    }

    @Override
    public Thermostat getByRoom(Room room, User user) {
        return thermostatRepository.findByRoom(room)
                .orElseThrow(() -> new ResourceNotFoundException("Thermostat not found"));
    }

    @Override
    public Thermostat getById(Long thermostatId, User user) {
        Thermostat thermostat = thermostatRepository.findById(thermostatId)
                .orElseThrow(() -> new ResourceNotFoundException("Thermostat not found: " + thermostatId));

        Room room = thermostat.getRoom();
        if (!room.getHome().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have access to this room");
        }

        return thermostat;
    }

    @Override
    public Thermostat setTargetTemperature(Long thermostatId, Double temperature, User user) {
        Thermostat thermostat = getById(thermostatId, user);
        thermostat.setTargetTemperature(temperature);

        return thermostatRepository.save(thermostat);
    }

    @Override
    public Thermostat setMode(Long thermostatId, ThermostatModes mode, User user) {
        Thermostat thermostat = getById(thermostatId, user);
        thermostat.setMode(mode);

        return thermostatRepository.save(thermostat);
    }

    @Override
    public Thermostat setStatus(Long thermostatId, ThermostatStatuses status, User user) {
        Thermostat thermostat = getById(thermostatId, user);
        thermostat.setStatus(status);

        return thermostatRepository.save(thermostat);
    }

}
