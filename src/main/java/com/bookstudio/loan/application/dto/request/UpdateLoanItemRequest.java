package com.bookstudio.loan.application.dto.request;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import com.bookstudio.loan.domain.model.type.LoanItemStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateLoanItemRequest(
    @NonNull
    @NotNull(message = "Copy ID is required")
    @Min(value = 1, message = "Copy ID must be at least 1")
    Long copyId,

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    LocalDate dueDate,

    @NotNull(message = "Status is required")
    LoanItemStatus status
) {}
