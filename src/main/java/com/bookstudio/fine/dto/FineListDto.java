package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.shared.enums.FineStatus;

public record FineListDto(
    String code,
    String loanCode,
    BigDecimal amount,
    Integer daysLate,
    FineStatus status,
    LocalDate issuedAt,
    Long id
) {}
