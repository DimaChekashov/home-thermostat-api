package com.example.home_thermostat_api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.TemperatureReading;
import com.example.home_thermostat_api.model.Thermostat;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.HomeRepository;
import com.example.home_thermostat_api.repository.TemperatureReadingRepository;

@Service
public class ExcelReportServiceImpl implements ExcelReportService {

    @Autowired
    private TemperatureReadingRepository temperatureReadingRepository;

    @Autowired
    private HomeRepository homeRepository;

    @Override
    public byte[] generateTemperatureReport(Long homeId, User user, int days) throws IOException {
        Home home = homeRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Home not found"));

        if (!home.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        LocalDateTime after = LocalDateTime.now().minusDays(days);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Отчёт по температуре");

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Отчёт по температуре: " + home.getName());
            titleCell.setCellStyle(createTitleStyle(workbook));

            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0)
                    .setCellValue("Период: " + after.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                            + " — " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

            Row headerRow = sheet.createRow(3);
            String[] headers = { "Дата/Время", "Комната", "Температура (°C)", "Источник" };
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 4;
            for (Room room : home.getRooms()) {
                Thermostat thermostat = room.getThermostat();
                if (thermostat == null)
                    continue;

                List<TemperatureReading> readings = temperatureReadingRepository
                        .findByThermostatAndTimestampAfterOrderByTimestampDesc(thermostat, after);

                for (TemperatureReading reading : readings) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(
                            reading.getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                    row.createCell(1).setCellValue(room.getName());
                    row.createCell(2).setCellValue(reading.getValue());
                    row.createCell(3).setCellValue(reading.getSource().name());
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Override
    public byte[] generateSummaryReport(User user) throws IOException {
        List<Home> homes = homeRepository.findAllByUser(user);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Сводка по домам");

            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("Сводный отчёт по системе Home Thermostat");
            titleRow.getCell(0).setCellStyle(createTitleStyle(workbook));

            Row userRow = sheet.createRow(1);
            userRow.createCell(0).setCellValue("Пользователь: " + user.getUsername());

            Row dateRow = sheet.createRow(2);
            dateRow.createCell(0).setCellValue("Дата: " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

            Row headerRow = sheet.createRow(4);
            String[] headers = { "Дом", "Комната", "Целевая t°", "Текущая t°", "Режим", "Статус" };
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 5;
            for (Home home : homes) {
                for (Room room : home.getRooms()) {
                    Thermostat thermostat = room.getThermostat();
                    if (thermostat == null)
                        continue;

                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(home.getName());
                    row.createCell(1).setCellValue(room.getName());
                    row.createCell(2).setCellValue(thermostat.getTargetTemperature());

                    Double currentTemp = temperatureReadingRepository
                            .findTopByThermostatOrderByTimestampDesc(thermostat)
                            .map(TemperatureReading::getValue)
                            .orElse(null);

                    row.createCell(3).setCellValue(currentTemp != null ? currentTemp : 0);
                    row.createCell(4).setCellValue(thermostat.getMode().name());
                    row.createCell(5).setCellValue(thermostat.getStatus().name());
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }
}
