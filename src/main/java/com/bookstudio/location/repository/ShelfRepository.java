package com.bookstudio.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.location.dto.ShelfDto;
import com.bookstudio.location.model.Location;
import com.bookstudio.location.model.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    @Query("""
        SELECT new com.bookstudio.location.dto.ShelfDto(
            s.shelfId,
            s.code,
            s.floor,
            s.description
        )
        FROM Shelf s
        WHERE s.location.locationId = :locationId
    """)
    List<ShelfDto> findShelfDtosByLocationId(@Param("locationId") Long locationId);

    void deleteAllByLocation(Location location);

    List<Shelf> findAllByOrderByShelfIdDesc();
}
