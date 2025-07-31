package com.bookstudio.location.service;

import com.bookstudio.location.dto.LocationResponseDto;
import com.bookstudio.location.dto.CreateLocationDto;
import com.bookstudio.location.dto.LocationDto;
import com.bookstudio.location.dto.UpdateLocationDto;
import com.bookstudio.location.model.Location;
import com.bookstudio.location.projection.LocationInfoProjection;
import com.bookstudio.location.projection.LocationListProjection;
import com.bookstudio.location.projection.LocationSelectProjection;
import com.bookstudio.location.repository.LocationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationListProjection> getList() {
        return locationRepository.findList();
    }

    public Optional<Location> findById(Long locationId) {
        return locationRepository.findById(locationId);
    }

    public Optional<LocationInfoProjection> getInfoById(Long locationId) {
        return locationRepository.findInfoById(locationId);
    }

    @Transactional
    public LocationResponseDto create(CreateLocationDto dto) {
        Location location = new Location();
        location.setName(dto.getName());
        location.setDescription(dto.getDescription());

        Location saved = locationRepository.save(location);

        return new LocationResponseDto(
                saved.getLocationId(),
                saved.getName(),
                saved.getDescription());
    }

    @Transactional
    public LocationResponseDto update(UpdateLocationDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + dto.getLocationId()));

        location.setName(dto.getName());
        location.setDescription(dto.getDescription());

        Location saved = locationRepository.save(location);

        return new LocationResponseDto(
                saved.getLocationId(),
                saved.getName(),
                saved.getDescription());
    }

    public List<LocationSelectProjection> getForSelect() {
        return locationRepository.findForSelect();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .id(location.getLocationId())
                .name(location.getName())
                .description(location.getDescription())
                .build();
    }
}
