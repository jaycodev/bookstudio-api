package com.bookstudio.reservation.dto;

import java.time.LocalDate;

import com.bookstudio.reservation.model.ReservationStatus;

public record ReservationListDto(
    Long id,
    String code,
    String readerCode,
    String readerFullName,
    String copyCode,
    LocalDate reservationDate,
    ReservationStatus status
) {}