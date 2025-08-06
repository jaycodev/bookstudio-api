package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.shared.enums.PaymentMethod;

public record PaymentListDto(
    String code,
    Long fineCount,
    String readerCode,
    String readerFullName,
    BigDecimal amount,
    LocalDate paymentDate,
    PaymentMethod method,
    Long id
) {}