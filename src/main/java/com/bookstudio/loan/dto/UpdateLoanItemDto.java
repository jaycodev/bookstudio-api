package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.loan.relation.LoanItemStatus;

import lombok.Data;

@Data
public class UpdateLoanItemDto {
    private Long copyId;
    private LocalDate dueDate;
    private LoanItemStatus status;
}
