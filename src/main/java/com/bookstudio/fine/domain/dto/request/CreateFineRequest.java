package com.bookstudio.fine.domain.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.fine.domain.model.FineStatus;
import com.bookstudio.loan.domain.model.LoanItemId;

import lombok.Data;

@Data
public class CreateFineRequest {
    private LoanItemId loanItemId;
    private BigDecimal amount;
    private Integer daysLate;
    private FineStatus status;
    private LocalDate issuedAt;
}
