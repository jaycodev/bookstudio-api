package com.bookstudio.loan.dto;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.reader.dto.ReaderSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailDto {
    private Long id;
    private String code;
    private ReaderSummaryDto reader;
    private LocalDate loanDate;
    private String observation;
    private List<LoanItemSummaryDto> items;
}
