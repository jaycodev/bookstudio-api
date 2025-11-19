package com.bookstudio.payment.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;

import com.bookstudio.payment.domain.model.type.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record UpdatePaymentRequest(
    @NonNull
    @NotNull(message = "Reader ID is required")
    @Min(value = 1, message = "Reader ID must be at least 1")
    Long readerId,

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00")
    BigDecimal amount,

    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date must be in the past or present")
    LocalDate paymentDate,

    @NotNull(message = "Method is required")
    PaymentMethod method,

    @NonNull
    @NotEmpty(message = "Fine IDs cannot be empty")
    List<Long> fineIds
) {}
