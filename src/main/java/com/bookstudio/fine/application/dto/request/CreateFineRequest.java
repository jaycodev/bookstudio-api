package com.bookstudio.fine.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.lang.NonNull;

import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.loan.domain.model.LoanItemId;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateFineRequest(
    @NonNull
    @NotNull(message = "Loan item ID is required")
    LoanItemId loanItemId,

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", message = "Amount must be at least 0.00")
    BigDecimal amount,

    @NotNull(message = "Days late is required")
    @Min(value = 1, message = "Days late must be at least 1")
    Integer daysLate,

    @NotNull(message = "Status is required")
    FineStatus status,

    @NotNull(message = "Issued at is required")
    @PastOrPresent(message = "Issued at must be in the past or present")
    LocalDate issuedAt
) {}
