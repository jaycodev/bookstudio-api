package com.bookstudio.fine.dto;

import lombok.*;
import java.math.BigDecimal;

import com.bookstudio.fine.model.FineStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FineSummaryDto {
    private Long id;
    private String code;
    private BigDecimal amount;
    private FineStatus status;
}
