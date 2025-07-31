package com.bookstudio.fine.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.loan.dto.LoanItemDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FineDto {
    private Long id;
    private String code;
    private LoanItemDto loanItem;
    private BigDecimal amount;
    private Integer daysLate;
    private String status;
    private LocalDate issuedAt;
}
