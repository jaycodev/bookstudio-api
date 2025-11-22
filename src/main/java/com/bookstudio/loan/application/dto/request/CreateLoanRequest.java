package com.bookstudio.loan.application.dto.request;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateLoanRequest(
    @NotNull(message = "Reader ID is required")
    @Min(value = 1, message = "Reader ID must be at least 1")
    Long readerId,

    String observation,

    @NotEmpty(message = "Items cannot be empty")
    List<CreateLoanItemRequest> items
) {}
