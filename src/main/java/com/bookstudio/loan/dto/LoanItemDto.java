package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.copy.dto.CopyDto;
import com.bookstudio.loan.relation.LoanItemId;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanItemDto {
    private LoanItemId id;
    private LoanDto loan;
    private CopyDto copy;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
}
