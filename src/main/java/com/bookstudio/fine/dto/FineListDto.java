package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.fine.model.FineStatus;

public record FineListDto(
    Long id,
    String code,
    Long loanId,
    String loanCode,
    Long copyId,
    String copyCode,
    BigDecimal amount,
    Integer daysLate,
    LocalDate issuedAt,
    FineStatus status
) {}
