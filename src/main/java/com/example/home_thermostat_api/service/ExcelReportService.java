package com.example.home_thermostat_api.service;

import java.io.IOException;

import com.example.home_thermostat_api.model.User;

public interface ExcelReportService {
    byte[] generateTemperatureReport(Long homeId, User user, int days) throws IOException;

    byte[] generateSummaryReport(User user) throws IOException;
}
