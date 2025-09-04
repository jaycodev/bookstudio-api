package com.bookstudio.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.location.dto.ShelfOptionDto;
import com.bookstudio.location.dto.ShelfSummaryDto;
import com.bookstudio.location.model.Location;
import com.bookstudio.location.model.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    @Query("""
        SELECT new com.bookstudio.location.dto.ShelfSummaryDto(
            s.shelfId,
            s.code,
            s.floor,
            s.description
        )
        FROM Shelf s
        WHERE s.location.locationId = :locationId
    """)
    List<ShelfSummaryDto> findShelfSummariesByLocationId(@Param("locationId") Long locationId);

    void deleteAllByLocation(Location location);

    @Query("""
        SELECT new com.bookstudio.location.dto.ShelfOptionDto(
            s.shelfId,
            CONCAT(s.location.name, ' - ', s.floor)
        )
        FROM Shelf s
        ORDER BY s.location.name ASC, s.floor ASC
    """)
    List<ShelfOptionDto> findForOptions();
}
