package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.loan.dto.LoanItemSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FineDetailDto {
    private Long id;
    private String code;
    private LoanItemSummaryDto loanItem;
    private BigDecimal amount;
    private Integer daysLate;
    private String status;
    private LocalDate issuedAt;
}
