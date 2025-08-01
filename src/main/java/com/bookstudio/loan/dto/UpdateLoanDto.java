package com.bookstudio.loan.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateLoanDto {
    private Long id;
    private Long readerId;
    private String observation;
    private List<UpdateLoanItemDto> items;
}
