package com.bookstudio.loan.domain.dto.request;

import java.time.LocalDate;

import com.bookstudio.loan.domain.model.LoanItemStatus;

import lombok.Data;

@Data
public class UpdateLoanItemRequest {
    private Long copyId;
    private LocalDate dueDate;
    private LoanItemStatus status;
}
