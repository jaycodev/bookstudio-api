package com.bookstudio.loan.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanResponseDto {
    private Long loanId;
    private String code;

    private String readerId;
    private String readerCode;
    private String readerFullName;

    private LocalDate loanDate;
}
