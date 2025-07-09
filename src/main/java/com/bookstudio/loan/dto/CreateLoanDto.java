package com.bookstudio.loan.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateLoanDto {
    private Long bookId;
    private Long studentId;
    private LocalDate returnDate;
    private int quantity;
    private String observation;
}
