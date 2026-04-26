package com.example.home_thermostat_api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.home_thermostat_api.model.TemperatureReading;

public interface DataImportService {
    List<TemperatureReading> importFromExcel(Long thermostatId, MultipartFile file) throws IOException;
}
