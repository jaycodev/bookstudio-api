package com.bookstudio.fine.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.loan.domain.model.LoanItemId;

public record CreateFineRequest(
    LoanItemId loanItemId,
    BigDecimal amount,
    Integer daysLate,
    FineStatus status,
    LocalDate issuedAt
) {}
