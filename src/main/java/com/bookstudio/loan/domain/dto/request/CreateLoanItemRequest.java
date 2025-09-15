package com.bookstudio.loan.domain.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateLoanItemRequest {
    private Long copyId;
    private LocalDate dueDate;
}
