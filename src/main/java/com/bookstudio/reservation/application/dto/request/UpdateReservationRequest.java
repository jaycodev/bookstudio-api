package com.bookstudio.reservation.application.dto.request;

import java.time.LocalDate;

import com.bookstudio.reservation.domain.model.type.ReservationStatus;

public record UpdateReservationRequest(
    Long readerId,
    Long copyId,
    LocalDate reservationDate,
    ReservationStatus status
) {}
