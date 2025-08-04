package com.bookstudio.reservation.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.ReservationStatus;

public record ReservationListDto(
    String code,
    String readerFullName,
    String copyCode,
    LocalDate reservationDate,
    ReservationStatus status,
    Long id
) {}