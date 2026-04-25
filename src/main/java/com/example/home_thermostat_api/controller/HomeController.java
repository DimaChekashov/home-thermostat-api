package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.dto.HomeRequest;
import com.example.home_thermostat_api.model.Home;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.HomeService;
import com.example.home_thermostat_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/homes")
@RestController
public class HomeController {
    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get user homes", description = "Returns all homes of the current user")
    @ApiResponse(responseCode = "200", description = "List of homes")
    public ResponseEntity<List<Home>> getUserHomes(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(homeService.getUserHomes(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get home by ID", description = "Returns home details")
    @ApiResponse(responseCode = "200", description = "Home found")
    @ApiResponse(responseCode = "404", description = "Home not found")
    public ResponseEntity<Home> getHome(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(homeService.getById(id, user));
    }

    @PostMapping
    @Operation(summary = "Create home", description = "Creates a new home")
    @ApiResponse(responseCode = "201", description = "Home created")
    public ResponseEntity<Home> createHome(@RequestBody HomeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Home home = homeService.create(request.name(), request.address(), user);

        return ResponseEntity.status(HttpStatus.CREATED).body(home);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update home", description = "Updates home information")
    @ApiResponse(responseCode = "200", description = "Home updated")
    public ResponseEntity<Home> updateHome(@PathVariable Long id,
            @RequestBody HomeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(homeService.update(id, request.name(), request.address(), user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete home", description = "Deletes a home")
    @ApiResponse(responseCode = "200", description = "Home deleted")
    public ResponseEntity<Void> deleteHome(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        homeService.delete(id, user);

        return ResponseEntity.ok().build();
    }
}
