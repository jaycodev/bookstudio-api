package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.fine.model.FineStatus;
import com.bookstudio.loan.relation.LoanItemId;

import lombok.Data;

@Data
public class CreateFineDto {
    private LoanItemId loanItemId;
    private BigDecimal amount;
    private Integer daysLate;
    private FineStatus status;
    private LocalDate issuedAt;
}
