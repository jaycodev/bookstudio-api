package com.bookstudio.reservation.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstudio.reservation.domain.dto.response.ReservationDetailResponse;
import com.bookstudio.reservation.domain.dto.response.ReservationListResponse;
import com.bookstudio.reservation.domain.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        SELECT new com.bookstudio.reservation.domain.dto.response.ReservationListResponse(
            r.id,
            r.code,
            new com.bookstudio.reservation.domain.dto.response.ReservationListResponse$Reader(
                rd.id,
                rd.code,
                CONCAT(rd.firstName, ' ', rd.lastName)
            ),
            new com.bookstudio.reservation.domain.dto.response.ReservationListResponse$Copy(
                c.code
            ),
            r.reservationDate,
            r.status
        )
        FROM Reservation r
        JOIN r.reader rd
        JOIN r.copy c
        ORDER BY r.id DESC
    """)
    List<ReservationListResponse> findList();

    @Query("""
        SELECT new com.bookstudio.reservation.domain.dto.response.ReservationDetailResponse(
            r.id,
            r.code,
            new com.bookstudio.reservation.domain.dto.response.ReservationDetailResponse$Reader(
                rd.id,
                rd.code,
                CONCAT(rd.firstName, ' ', rd.lastName)
            ),
            new com.bookstudio.reservation.domain.dto.response.ReservationDetailResponse$Copy(
                c.id,
                c.code,
                c.barcode,
                c.status
            ),
            r.reservationDate,
            r.status
        )
        FROM Reservation r
        JOIN r.reader rd
        JOIN r.copy c
        WHERE r.id = :id
    """)
    Optional<ReservationDetailResponse> findDetailById(Long id);
}
