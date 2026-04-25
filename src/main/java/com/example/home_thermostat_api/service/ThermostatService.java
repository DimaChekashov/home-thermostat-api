package com.example.home_thermostat_api.service;

import com.example.home_thermostat_api.enums.ThermostatModes;
import com.example.home_thermostat_api.enums.ThermostatStatuses;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.model.User;

public interface ThermostatService {
    Thermostat create(Room room);

    Thermostat getByRoomId(Long roomId, User user);

    Thermostat getById(Long thermostatId, User user);

    Thermostat setTargetTemperature(Long thermostatId, Double temperature, User user);

    Thermostat setMode(Long thermostatId, ThermostatModes mode, User user);

    Thermostat setStatus(Long thermostatId, ThermostatStatuses status, User user);
}
