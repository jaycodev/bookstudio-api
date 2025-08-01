package com.bookstudio.loan.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateLoanItemDto {
    private Long copyId;
    private LocalDate dueDate;
}
