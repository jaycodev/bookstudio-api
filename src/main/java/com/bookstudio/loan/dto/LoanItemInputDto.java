package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.LoanItemStatus;

import lombok.Data;

@Data
public class LoanItemInputDto {
    private Long copyId;
    private LocalDate dueDate;
    private LoanItemStatus status;
}
