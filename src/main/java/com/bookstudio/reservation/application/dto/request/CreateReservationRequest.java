package com.bookstudio.reservation.application.dto.request;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import com.bookstudio.reservation.domain.model.type.ReservationStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateReservationRequest(
    @NonNull
    @NotNull(message = "Reader ID is required")
    @Min(value = 1, message = "Reader ID must be at least 1")
    Long readerId,

    @NonNull
    @NotNull(message = "Copy ID is required")
    @Min(value = 1, message = "Copy ID must be at least 1")
    Long copyId,

    @NotNull(message = "Reservation date is required")
    @PastOrPresent(message = "Reservation date must be in the past or present")
    LocalDate reservationDate,

    @NotNull(message = "Status is required")
    ReservationStatus status
) {}
