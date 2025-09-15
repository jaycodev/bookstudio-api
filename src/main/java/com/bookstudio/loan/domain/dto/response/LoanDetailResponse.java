package com.bookstudio.loan.domain.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.copy.domain.model.CopyStatus;
import com.bookstudio.loan.domain.model.LoanItemStatus;

public record LoanDetailResponse(
        Long id,
        String code,
        Reader reader,
        LocalDate loanDate,
        String observation,
        List<LoanItem> items) {

    public LoanDetailResponse withItems(List<LoanItem> items) {
        return new LoanDetailResponse(id, code, reader, loanDate, observation, items);
    }

    public record Reader(
            Long id,
            String code,
            String fullName) {
    }

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
