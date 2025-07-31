package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.copy.dto.CopyDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanItemDto {
    private CopyDto copy;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
}
