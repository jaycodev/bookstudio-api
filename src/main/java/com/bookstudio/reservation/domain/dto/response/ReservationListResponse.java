package com.bookstudio.reservation.domain.dto.response;

import java.time.LocalDate;

import com.bookstudio.reservation.domain.model.ReservationStatus;

public record ReservationListResponse(
        Long id,
        String code,
        Reader reader,
        Copy copy,
        LocalDate reservationDate,
        ReservationStatus status) {

    public record Reader(
            Long id,
            String code,
            String fullName) {
    }

    public record Copy(
            String code) {
    }
}
