package com.bookstudio.payment.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.payment.domain.model.type.PaymentMethod;

public record PaymentListResponse(
        Long id,
        String code,
        Long fineCount,
        Reader reader,
        BigDecimal amount,
        LocalDate paymentDate,
        PaymentMethod method) {

    public record Reader(
            Long id,
            String code,
            String fullName) {
    }
}