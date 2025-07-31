package com.bookstudio.loan.dto;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.reader.dto.ReaderDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {
    private Long id;
    private String code;
    private ReaderDto reader;
    private LocalDate loanDate;
    private String observation;
    private List<LoanItemDto> items;
}
