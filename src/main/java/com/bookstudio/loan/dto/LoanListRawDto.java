package com.bookstudio.loan.dto;

import java.time.LocalDate;

public record LoanListRawDto(
    String code,
    String readerCode,
    String readerFullName,
    LocalDate loanDate,
    Long borrowedCount,
    Long returnedCount,
    Long overdueCount,
    Long lostCount,
    Long canceledCount,
    Long id
) {}
