package com.bookstudio.reservation.dto;

import java.time.LocalDate;

import com.bookstudio.reservation.model.ReservationStatus;

import lombok.Data;

@Data
public class UpdateReservationDto {
    private Long readerId;
    private Long copyId;
    private LocalDate reservationDate;
    private ReservationStatus status;
}
