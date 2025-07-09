package com.bookstudio.loan.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateLoanDto {
    private Long loanId;
    private Long bookId;
    private Long studentId;
    private LocalDate returnDate;
    private String observation;
}
