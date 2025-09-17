package com.bookstudio.payment.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bookstudio.fine.domain.model.type.FineStatus;
import com.bookstudio.payment.domain.model.type.PaymentMethod;

public record PaymentDetailResponse(
        Long id,
        String code,
        Reader reader,
        BigDecimal amount,
        LocalDate paymentDate,
        PaymentMethod method,
        List<FineItem> fines) {

    public PaymentDetailResponse withFines(List<FineItem> fines) {
        return new PaymentDetailResponse(id, code, reader, amount, paymentDate, method, fines);
    }

    public record Reader(
            Long id,
            String code,
            String fullName) {
    }

    public record FineItem(
            Long id,
            String code,
            BigDecimal amount,
            FineStatus status) {
    }
}
