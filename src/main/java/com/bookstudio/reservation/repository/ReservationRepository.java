package com.bookstudio.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.reservation.model.Reservation;
import com.bookstudio.reservation.projection.ReservationInfoProjection;
import com.bookstudio.reservation.projection.ReservationListProjection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        SELECT
            r.reservationId AS reservationId,
            r.code AS code,
            CONCAT(rd.firstName, ' ', rd.lastName) AS readerName,
            c.code AS copyCode,
            r.reservationDate AS reservationDate,
            r.status AS status
        FROM Reservation r
        JOIN r.reader rd
        JOIN r.copy c
        ORDER BY r.reservationId DESC
    """)
    List<ReservationListProjection> findList();

    @Query("""
        SELECT
            r.reservationId AS reservationId,
            r.code AS code,
            CONCAT(rd.firstName, ' ', rd.lastName) AS readerName,
            c.code AS copyCode,
            r.reservationDate AS reservationDate,
            r.status AS status
        FROM Reservation r
        JOIN r.reader rd
        JOIN r.copy c
        WHERE r.reservationId = :id
    """)
    Optional<ReservationInfoProjection> findInfoById(@Param("id") Long id);
}
