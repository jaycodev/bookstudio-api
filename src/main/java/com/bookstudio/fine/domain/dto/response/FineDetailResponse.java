package com.bookstudio.fine.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.copy.domain.model.CopyStatus;
import com.bookstudio.fine.domain.model.FineStatus;
import com.bookstudio.loan.domain.model.LoanItemStatus;

public record FineDetailResponse(
        Long id,
        String code,
        LoanItem loanItem,
        BigDecimal amount,
        Integer daysLate,
        FineStatus status,
        LocalDate issuedAt) {

    public record LoanItem(
            Copy copy,
            LocalDate dueDate,
            LocalDate returnDate,
            LoanItemStatus status) {

        public record Copy(
                Long id,
                String code,
                String barcode,
                CopyStatus status) {
        }
    }
}
