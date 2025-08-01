package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.reader.dto.ReaderSummaryDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanSummaryDto {
    private Long id;
    private String code;
    private ReaderSummaryDto reader;
    private LocalDate loanDate;
}
