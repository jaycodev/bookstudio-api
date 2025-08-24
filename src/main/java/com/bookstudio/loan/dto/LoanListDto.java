package com.bookstudio.loan.dto;

import java.time.LocalDate;
import java.util.Map;

public record LoanListDto(
    Long id,
    String code,
    String readerCode,
    String readerFullName,
    LocalDate loanDate,
    int itemCount,
    Map<String, Long> statusCounts
) {}
