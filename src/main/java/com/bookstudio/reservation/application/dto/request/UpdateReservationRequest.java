package com.bookstudio.reservation.application.dto.request;

import java.time.LocalDate;

import com.bookstudio.reservation.domain.model.type.ReservationStatus;

import lombok.Data;

@Data
public class UpdateReservationRequest {
    private Long readerId;
    private Long copyId;
    private LocalDate reservationDate;
    private ReservationStatus status;
}
