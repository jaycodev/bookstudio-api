package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.fine.model.FineStatus;

public record FineListDto(
    Long id,
    String code,
    String loanCode,
    String copyCode,
    BigDecimal amount,
    Integer daysLate,
    LocalDate issuedAt,
    FineStatus status
) {}
