package com.bookstudio.location.presentation;

import com.bookstudio.location.application.LocationService;
import com.bookstudio.location.application.dto.request.CreateLocationRequest;
import com.bookstudio.location.application.dto.request.UpdateLocationRequest;
import com.bookstudio.location.application.dto.response.LocationDetailResponse;
import com.bookstudio.location.application.dto.response.LocationListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Operations related to locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    @Operation(summary = "List all locations")
    public ResponseEntity<?> list() {
        List<LocationListResponse> locations = locationService.getList();
        if (locations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No locations found.", "no_content", 204));
        }
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a location by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            LocationDetailResponse location = locationService.getDetailById(id);
            return ResponseEntity.ok(location);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new location")
    public ResponseEntity<?> create(@RequestBody CreateLocationRequest request) {
        try {
            LocationListResponse created = locationService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a location by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateLocationRequest request) {
        try {
            LocationListResponse updated = locationService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }
}
