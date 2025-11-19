package com.bookstudio.copy.application.dto.request;

import org.springframework.lang.NonNull;

import com.bookstudio.copy.domain.model.type.CopyCondition;
import com.bookstudio.copy.domain.model.type.CopyStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCopyRequest(
    @NonNull
    @NotNull(message = "Book ID is required")
    @Min(value = 1, message = "Book ID must be at least 1")
    Long bookId,

    @NonNull
    @NotNull(message = "Shelf ID is required")
    @Min(value = 1, message = "Shelf ID must be at least 1")
    Long shelfId,

    @NotBlank(message = "Barcode is required")
    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    String barcode,

    @NotNull(message = "Status is required")
    CopyStatus status,

    @NotNull(message = "Condition is required")
    CopyCondition condition
) {}
