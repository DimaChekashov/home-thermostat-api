package com.example.home_thermostat_api.service;

import java.util.List;

import com.example.home_thermostat_api.dto.UserDto;
import com.example.home_thermostat_api.dto.UserLoggedDto;

public interface UserService {
    public List<UserDto> getUsers();

    public UserDto create(UserDto userDto);

    public UserDto getUser(Long userId);

    public UserDto getUser(String username);

    public UserDto updateUser(Long userId, UserDto userDto);

    public String deleteUser(Long userId);

    UserLoggedDto getUserLoggedInfo();
}
