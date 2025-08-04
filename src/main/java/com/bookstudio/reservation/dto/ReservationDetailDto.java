package com.bookstudio.reservation.dto;

import java.time.LocalDate;

import com.bookstudio.copy.dto.CopySummaryDto;
import com.bookstudio.reader.dto.ReaderSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailDto {
    private Long id;
    private String code;
    private ReaderSummaryDto reader;
    private CopySummaryDto copy;
    private LocalDate reservationDate;
    private String status;
}
