package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FineResponseDto {
    private Long fineId;
    private String code;
    private String loanCode;
    private BigDecimal amount;
    private Integer daysLate;
    private String status;
    private LocalDate issuedAt;
}
