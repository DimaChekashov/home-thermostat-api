package com.example.home_thermostat_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {

    List<Home> findAllByUser(User user);
}
