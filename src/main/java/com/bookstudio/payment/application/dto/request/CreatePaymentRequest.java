package com.bookstudio.payment.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bookstudio.payment.domain.model.type.PaymentMethod;

public record CreatePaymentRequest(
    Long readerId,
    BigDecimal amount,
    LocalDate paymentDate,
    PaymentMethod method,
    List<Long> fineIds
) {}
