package com.bookstudio.location.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.location.model.Location;
import com.bookstudio.location.projection.LocationInfoProjection;
import com.bookstudio.location.projection.LocationListProjection;
import com.bookstudio.location.projection.LocationSelectProjection;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("""
        SELECT 
            l.locationId AS locationId,
            l.name AS name,
            l.description AS description
        FROM Location l
        ORDER BY l.locationId DESC
    """)
    List<LocationListProjection> findList();

    @Query("""
        SELECT 
            l.locationId AS locationId,
            l.name AS name,
            l.description AS description
        FROM Location l
        WHERE l.locationId = :id
    """)
    Optional<LocationInfoProjection> findInfoById(@Param("id") Long id);

    @Query("""
        SELECT 
            l.locationId AS locationId,
            l.name AS name
        FROM Location l
    """)
    List<LocationSelectProjection> findForSelect();
}
