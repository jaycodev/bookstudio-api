package com.bookstudio.loan.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateLoanDto {
    private Long readerId;
    private String observation;
    private List<CreateLoanItemDto> items;
}
