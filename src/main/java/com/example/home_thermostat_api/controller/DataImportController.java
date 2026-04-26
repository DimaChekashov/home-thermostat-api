package com.example.home_thermostat_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.service.DataImportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/import")
@Tag(name = "Data Import", description = "Import temperature data from files")
public class DataImportController {
    @Autowired
    private DataImportService dataImportService;

    @Operation(summary = "Import from Excel", description = "Upload Excel file with temperature readings")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/excel/{thermostatId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(
            @PathVariable Long thermostatId,
            @RequestParam("file") MultipartFile file) {
        try {
            List<TemperatureReading> readings = dataImportService.importFromExcel(thermostatId, file);
            return ResponseEntity.ok(Map.of(
                    "message", "Imported successfully",
                    "count", readings.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
