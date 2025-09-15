package com.bookstudio.location.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.location.domain.dto.response.LocationDetailResponse;
import com.bookstudio.location.domain.dto.response.LocationListResponse;
import com.bookstudio.location.domain.model.Location;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("""
        SELECT 
            l.id AS id,
            l.name AS name,
            l.description AS description
        FROM Location l
        ORDER BY l.id DESC
    """)
    List<LocationListResponse> findList();

    @Query("""
        SELECT 
            l.id AS value,
            l.name AS label
        FROM Location l
        ORDER BY l.name ASC
    """)
    List<OptionResponse> findForOptions();

    @Query("""
        SELECT 
            l.id AS id,
            l.name AS name,
            l.description AS description,
            NULL AS shelves
        FROM Location l
        WHERE l.id = :id
    """)
    Optional<LocationDetailResponse> findDetailById(Long id);
}
