package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.shared.enums.PaymentMethod;

public record PaymentListDto(
    String code,
    String readerFullName,
    BigDecimal amount,
    LocalDate paymentDate,
    PaymentMethod method,
    Long id
) {}