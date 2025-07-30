package com.bookstudio.loan.dto;

import lombok.Data;

@Data
public class CreateLoanDto {
    private Long readerId;
    private String observation;
}
