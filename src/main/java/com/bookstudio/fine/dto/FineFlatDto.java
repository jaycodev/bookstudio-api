package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.shared.enums.FineStatus;
import com.bookstudio.shared.enums.LoanItemStatus;

public record FineFlatDto(
    Long id,
    String code,

    Long loanId,
    Long copyId,
    LocalDate dueDate,
    LocalDate returnDate,
    LoanItemStatus loanItemStatus,
    
    String copyCode,

    BigDecimal amount,
    Integer daysLate,
    FineStatus status,
    LocalDate issuedAt
) {}

