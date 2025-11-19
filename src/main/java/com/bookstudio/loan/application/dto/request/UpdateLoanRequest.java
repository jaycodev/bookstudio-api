package com.bookstudio.loan.application.dto.request;

import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateLoanRequest(
    @NonNull
    @NotNull(message = "Reader ID is required")
    @Min(value = 1, message = "Reader ID must be at least 1")
    Long readerId,

    String observation,

    @NonNull
    @NotEmpty(message = "Items cannot be empty")
    List<UpdateLoanItemRequest> items
) {}
