package com.bookstudio.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.reservation.dto.ReservationListDto;
import com.bookstudio.reservation.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        SELECT new com.bookstudio.reservation.dto.ReservationListDto(
            r.reservationId,
            r.code,
            rd.code,
            CONCAT(rd.firstName, ' ', rd.lastName),
            c.code,
            r.reservationDate,
            r.status
        )
        FROM Reservation r
        JOIN r.reader rd
        JOIN r.copy c
        ORDER BY r.reservationId DESC
    """)
    List<ReservationListDto> findList();
}
