package com.bookstudio.location.controller;

import com.bookstudio.location.dto.LocationResponseDto;
import com.bookstudio.location.dto.CreateLocationDto;
import com.bookstudio.location.dto.UpdateLocationDto;
import com.bookstudio.location.projection.LocationInfoProjection;
import com.bookstudio.location.projection.LocationListProjection;
import com.bookstudio.location.service.LocationService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<LocationListProjection> locations = locationService.getList();
        if (locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No locations found.", "no_content", 204));
        }
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        LocationInfoProjection location = locationService.getInfoById(id).orElse(null);
        if (location == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Location not found.", "not_found", 404));
        }
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateLocationDto dto) {
        try {
            LocationResponseDto created = locationService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateLocationDto dto) {
        try {
            LocationResponseDto updated = locationService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }
}
