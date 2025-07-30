package com.bookstudio.loan.dto;

import lombok.Data;

@Data
public class UpdateLoanDto {
    private Long loanId;
    private Long readerId;
    private String observation;
}
