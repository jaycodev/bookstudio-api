package com.bookstudio.location.application;

import com.bookstudio.copy.infrastructure.repository.CopyRepository;
import com.bookstudio.location.application.dto.request.CreateLocationRequest;
import com.bookstudio.location.application.dto.request.CreateShelfRequest;
import com.bookstudio.location.application.dto.request.UpdateLocationRequest;
import com.bookstudio.location.application.dto.request.UpdateShelfRequest;
import com.bookstudio.location.application.dto.response.LocationDetailResponse;
import com.bookstudio.location.application.dto.response.LocationListResponse;
import com.bookstudio.location.domain.model.Location;
import com.bookstudio.location.domain.model.Shelf;
import com.bookstudio.location.infrastructure.repository.LocationRepository;
import com.bookstudio.location.infrastructure.repository.ShelfRepository;
import com.bookstudio.shared.application.dto.response.OptionResponse;

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
        location.setName(request.name());
        location.setDescription(request.description());

        Location saved = locationRepository.save(location);
        entityManager.refresh(saved);

        if (request.shelves() != null) {
            for (CreateShelfRequest shelfDto : request.shelves()) {
                Shelf shelf = new Shelf();
                shelf.setCode(shelfDto.code());
                shelf.setFloor(shelfDto.floor());
                shelf.setDescription(shelfDto.description());
                shelf.setLocation(saved);

                shelfRepository.save(shelf);
            }
        }

        return toListResponse(saved);
    }

    @Transactional
    public LocationListResponse update(Long id, UpdateLocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));

        location.setName(request.name());
        location.setDescription(request.description());

        if (request.shelves() != null) {
            for (UpdateShelfRequest shelfRequest : request.shelves()) {
                if (shelfRequest.id() != null) {
                    Shelf shelf = shelfRepository.findById(shelfRequest.id())
                            .orElseThrow(
                                    () -> new EntityNotFoundException(
                                            "Shelf not found with ID: " + shelfRequest.id()));

                    shelf.setCode(shelfRequest.code());
                    shelf.setFloor(shelfRequest.floor());
                    shelf.setDescription(shelfRequest.description());
                } else {
                    Shelf shelf = new Shelf();
                    shelf.setCode(shelf.getCode());
                    shelf.setFloor(shelf.getFloor());
                    shelf.setDescription(shelf.getDescription());
                    shelf.setLocation(location);

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
