package com.example.home_thermostat_api.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.ExcelReportService;
import com.example.home_thermostat_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/reports")
public class ExcelReportController {
    @Autowired
    private ExcelReportService excelReportService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Download temperature report", description = "Generates Excel report for a home")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/temperature/{homeId}")
    public ResponseEntity<byte[]> downloadTemperatureReport(
            @PathVariable Long homeId,
            @RequestParam(defaultValue = "7") int days,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        User user = userService.getUserByUsername(userDetails.getUsername());
        byte[] report = excelReportService.generateTemperatureReport(homeId, user, days);

        String filename = "temperature_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(report);
    }

    @Operation(summary = "Download summary report", description = "Generates summary Excel report for all homes")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/summary")
    public ResponseEntity<byte[]> downloadSummaryReport(
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        User user = userService.getUserByUsername(userDetails.getUsername());
        byte[] report = excelReportService.generateSummaryReport(user);

        String filename = "summary_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(report);
    }
}
