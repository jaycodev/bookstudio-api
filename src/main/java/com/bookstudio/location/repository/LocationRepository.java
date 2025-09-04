package com.bookstudio.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.location.dto.LocationListDto;
import com.bookstudio.location.dto.LocationOptionDto;
import com.bookstudio.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("""
        SELECT 
            new com.bookstudio.location.dto.LocationListDto(
                l.locationId,
                l.name,
                l.description
            )
        FROM Location l
        ORDER BY l.locationId DESC
    """)
    List<LocationListDto> findList();

    @Query("""
        SELECT 
            new com.bookstudio.location.dto.LocationOptionDto(
                l.locationId,
                l.name
            )
        FROM Location l
        ORDER BY l.name ASC
    """)
    List<LocationOptionDto> findForOptions();
}
