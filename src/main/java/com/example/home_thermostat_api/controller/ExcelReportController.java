package com.example.home_thermostat_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.home_thermostat_api.service.ExcelReportService;

@RestController
@RequestMapping("/api/reports")
public class ExcelReportController {

    @Autowired
    private ExcelReportService excelReportService;

}
