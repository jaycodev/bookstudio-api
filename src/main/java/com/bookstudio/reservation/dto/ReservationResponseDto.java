package com.bookstudio.reservation.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private String code;
    private String readerName;
    private String copyCode;
    private LocalDate reservationDate;
    private String status;
}
