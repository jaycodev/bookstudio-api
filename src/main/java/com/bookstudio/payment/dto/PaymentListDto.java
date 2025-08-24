package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.shared.enums.PaymentMethod;

public record PaymentListDto(
    Long id,
    String code,
    Long fineCount,
    String readerCode,
    String readerFullName,
    BigDecimal amount,
    LocalDate paymentDate,
    PaymentMethod method
) {}