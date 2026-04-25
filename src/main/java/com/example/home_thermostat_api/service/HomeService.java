package com.example.home_thermostat_api.service;

import java.util.List;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;

public interface HomeService {

    Home create(String name, String address, User user);

    List<Home> getUserHomes(User user);

    Home getById(Long homeId, User user);

    void delete(Long homeId, User user);

    Home update(Long homeId, String name, String address, User user);

}
