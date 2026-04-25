package com.example.home_thermostat_api.controller;

import com.example.home_thermostat_api.dto.RoomRequest;
import com.example.home_thermostat_api.model.Room;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.service.RoomService;
import com.example.home_thermostat_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/rooms")
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get rooms in home", description = "Returns all rooms in a home")
    @ApiResponse(responseCode = "200", description = "List of rooms")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Room>> getRooms(
            @PathVariable Long homeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(roomService.getRoomsByHomeId(homeId, user));
    }

    @Operation(summary = "Get room by ID", description = "Returns room details")
    @ApiResponse(responseCode = "200", description = "Room found")
    @ApiResponse(responseCode = "404", description = "Room not found")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(
            @PathVariable Long homeId,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(roomService.getById(id, user));
    }

    @Operation(summary = "Create room", description = "Adds a new room to a home")
    @ApiResponse(responseCode = "201", description = "Room created")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Room> createRoom(
            @PathVariable Long homeId,
            @RequestBody RoomRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Room room = roomService.create(request.name(), homeId, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @Operation(summary = "Update room", description = "Updates room information")
    @ApiResponse(responseCode = "200", description = "Room updated")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long homeId,
            @PathVariable Long id,
            @RequestBody RoomRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(roomService.update(id, request.name(), user));
    }

    @Operation(summary = "Delete room", description = "Deletes a room")
    @ApiResponse(responseCode = "200", description = "Room deleted")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long homeId,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        roomService.delete(id, user);

        return ResponseEntity.ok().build();
    }
}
