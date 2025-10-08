package com.bookstudio.loan.application.dto.request;

import java.time.LocalDate;

import com.bookstudio.loan.domain.model.type.LoanItemStatus;

public record UpdateLoanItemRequest(
    Long copyId,
    LocalDate dueDate,
    LoanItemStatus status
) {}
