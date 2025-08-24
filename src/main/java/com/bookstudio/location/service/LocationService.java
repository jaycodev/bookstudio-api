package com.bookstudio.location.service;

import com.bookstudio.location.dto.CreateLocationDto;
import com.bookstudio.location.dto.CreateShelfDto;
import com.bookstudio.location.dto.LocationDetailDto;
import com.bookstudio.location.dto.LocationListDto;
import com.bookstudio.location.dto.LocationSelectDto;
import com.bookstudio.location.dto.LocationSummaryDto;
import com.bookstudio.location.dto.UpdateLocationDto;
import com.bookstudio.location.dto.UpdateShelfDto;
import com.bookstudio.location.model.Location;
import com.bookstudio.location.model.Shelf;
import com.bookstudio.location.repository.LocationRepository;
import com.bookstudio.location.repository.ShelfRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LocationRepository locationRepository;
    private final ShelfRepository shelfRepository;

    public List<LocationListDto> getList() {
        return locationRepository.findList();
    }

    public Optional<Location> findById(Long locationId) {
        return locationRepository.findById(locationId);
    }

    public LocationDetailDto getInfoById(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + locationId));

        return LocationDetailDto.builder()
                .id(location.getLocationId())
                .name(location.getName())
                .description(location.getDescription())
                .shelves(shelfRepository.findShelfSummariesByLocationId(location.getLocationId()))
                .build();
    }

    @Transactional
    public LocationListDto create(CreateLocationDto dto) {
        Location location = new Location();
        location.setName(dto.getName());
        location.setDescription(dto.getDescription());

        Location saved = locationRepository.save(location);
        entityManager.refresh(saved);

        if (dto.getShelves() != null) {
            for (CreateShelfDto shelfDto : dto.getShelves()) {
                Shelf shelf = Shelf.builder()
                        .code(shelfDto.getCode())
                        .floor(shelfDto.getFloor())
                        .description(shelfDto.getDescription())
                        .location(saved)
                        .build();
                shelfRepository.save(shelf);
            }
        }

        return toListDto(saved);
    }

    @Transactional
    public LocationListDto update(Long locationId, UpdateLocationDto dto) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + locationId));

        location.setName(dto.getName());
        location.setDescription(dto.getDescription());
        locationRepository.save(location);

        if (dto.getShelves() != null) {
            for (UpdateShelfDto shelfDto : dto.getShelves()) {
                if (shelfDto.getId() != null) {
                    Shelf shelf = shelfRepository.findById(shelfDto.getId())
                            .orElseThrow(
                                    () -> new EntityNotFoundException("Shelf not found with ID: " + shelfDto.getId()));

                    shelf.setCode(shelfDto.getCode());
                    shelf.setFloor(shelfDto.getFloor());
                    shelf.setDescription(shelfDto.getDescription());
                    shelfRepository.save(shelf);
                } else {
                    Shelf shelf = Shelf.builder()
                            .code(shelfDto.getCode())
                            .floor(shelfDto.getFloor())
                            .description(shelfDto.getDescription())
                            .location(location)
                            .build();
                    shelfRepository.save(shelf);
                }
            }
        }

        return toListDto(location);
    }

    public List<LocationSelectDto> getForSelect() {
        return locationRepository.findForSelect();
    }

    public LocationSummaryDto toSummaryDto(Location location) {
        return LocationSummaryDto.builder()
                .id(location.getLocationId())
                .name(location.getName())
                .build();
    }

    private LocationListDto toListDto(Location location) {
        return new LocationListDto(
                location.getLocationId(),
                location.getName(),
                location.getDescription());
    }
}
