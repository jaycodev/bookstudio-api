package com.bookstudio.loan.dto;

import java.time.LocalDate;

public record LoanListDto(
    String code,
    String readerCode,
    String readerFullName,
    LocalDate loanDate,
    String observation,
    Long id
) {}
