package com.bookstudio.fine.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.fine.domain.model.type.FineStatus;

public record FineListResponse(
        Long id,
        String code,
        Loan loan,
        Copy copy,
        BigDecimal amount,
        Integer daysLate,
        LocalDate issuedAt,
        FineStatus status) {

    public record Loan(
            Long id,
            String code) {
    }

    public record Copy(
            Long id,
            String code) {
    }
}
