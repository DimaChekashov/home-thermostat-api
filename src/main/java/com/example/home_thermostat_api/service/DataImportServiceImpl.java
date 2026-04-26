package com.example.home_thermostat_api.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.home_thermostat_api.enums.TemperatureReadingSources;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.repository.TemperatureReadingRepository;
import com.example.home_thermostat_api.repository.ThermostatRepository;

@Service
public class DataImportServiceImpl implements DataImportService {
    @Autowired
    private ThermostatRepository thermostatRepository;

    @Autowired
    private TemperatureReadingRepository temperatureReadingRepository;

    @Override
    public List<TemperatureReading> importFromExcel(Long thermostatId, MultipartFile file) throws IOException {
        Thermostat thermostat = thermostatRepository.findById(thermostatId)
                .orElseThrow(() -> new RuntimeException("Thermostat not found"));

        List<TemperatureReading> readings = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                Double value = row.getCell(0).getNumericCellValue();

                LocalDateTime timestamp;
                Cell dateCell = row.getCell(1);
                if (dateCell.getCellType() == CellType.NUMERIC) {
                    timestamp = dateCell.getLocalDateTimeCellValue();
                } else {
                    timestamp = LocalDateTime.parse(dateCell.getStringCellValue(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }

                String sourceStr = row.getCell(2) != null
                        ? row.getCell(2).getStringCellValue().toUpperCase()
                        : "IMPORTED";

                TemperatureReading reading = new TemperatureReading();
                reading.setTemperature(value);
                reading.setSource(TemperatureReadingSources.valueOf(sourceStr));
                reading.setThermostat(thermostat);
                reading.setTimestamp(timestamp);
                readings.add(reading);
            }
        }

        return temperatureReadingRepository.saveAll(readings);
    }
}
