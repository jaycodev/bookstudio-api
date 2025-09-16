package com.bookstudio.location.application;

import com.bookstudio.copy.infrastructure.repository.CopyRepository;
import com.bookstudio.location.domain.dto.request.CreateLocationRequest;
import com.bookstudio.location.domain.dto.request.CreateShelfRequest;
import com.bookstudio.location.domain.dto.request.UpdateLocationRequest;
import com.bookstudio.location.domain.dto.request.UpdateShelfRequest;
import com.bookstudio.location.domain.dto.response.LocationDetailResponse;
import com.bookstudio.location.domain.dto.response.LocationListResponse;
import com.bookstudio.location.domain.model.Location;
import com.bookstudio.location.domain.model.Shelf;
import com.bookstudio.location.infrastructure.repository.LocationRepository;
import com.bookstudio.location.infrastructure.repository.ShelfRepository;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LocationRepository locationRepository;
    private final ShelfRepository shelfRepository;
    private final CopyRepository copyRepository;

    public List<LocationListResponse> getList() {
        return locationRepository.findList();
    }

    public List<OptionResponse> getOptions() {
        return locationRepository.findForOptions();
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public LocationDetailResponse getDetailById(Long id) {
        LocationDetailResponse base = locationRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));

        return base.withShelves(shelfRepository.findShelfItemsByLocationId(id));
    }

    @Transactional
    public LocationListResponse create(CreateLocationRequest request) {
        Location location = new Location();
        location.setName(request.getName());
        location.setDescription(request.getDescription());

        Location saved = locationRepository.save(location);
        entityManager.refresh(saved);

        if (request.getShelves() != null) {
            for (CreateShelfRequest shelfDto : request.getShelves()) {
                Shelf shelf = Shelf.builder()
                        .code(shelfDto.getCode())
                        .floor(shelfDto.getFloor())
                        .description(shelfDto.getDescription())
                        .location(saved)
                        .build();
                shelfRepository.save(shelf);
            }
        }

        return toListResponse(saved);
    }

    @Transactional
    public LocationListResponse update(Long id, UpdateLocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));

        location.setName(request.getName());
        location.setDescription(request.getDescription());

        if (request.getShelves() != null) {
            for (UpdateShelfRequest shelfRequest : request.getShelves()) {
                if (shelfRequest.getId() != null) {
                    Shelf shelf = shelfRepository.findById(shelfRequest.getId())
                            .orElseThrow(
                                    () -> new EntityNotFoundException(
                                            "Shelf not found with ID: " + shelfRequest.getId()));

                    shelf.setCode(shelfRequest.getCode());
                    shelf.setFloor(shelfRequest.getFloor());
                    shelf.setDescription(shelfRequest.getDescription());
                } else {
                    Shelf shelf = Shelf.builder()
                            .code(shelfRequest.getCode())
                            .floor(shelfRequest.getFloor())
                            .description(shelfRequest.getDescription())
                            .location(location)
                            .build();
                    shelfRepository.save(shelf);
                }
            }
        }

        return toListResponse(location);
    }

    private LocationListResponse toListResponse(Location location) {
        return new LocationListResponse(
                location.getId(),
                location.getName(),
                location.getDescription(),
                shelfRepository.countByLocation(location),
                copyRepository.countDistinctBookByShelfLocation(location),
                copyRepository.countByShelfLocation(location));
    }
}
