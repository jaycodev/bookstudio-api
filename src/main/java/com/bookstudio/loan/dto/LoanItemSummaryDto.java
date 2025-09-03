package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.copy.dto.CopySummaryDto;
import com.bookstudio.loan.relation.LoanItemStatus;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanItemSummaryDto {
    private CopySummaryDto copy;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanItemStatus status;
}
