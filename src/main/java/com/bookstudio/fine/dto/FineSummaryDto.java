package com.bookstudio.fine.dto;

import lombok.*;
import java.math.BigDecimal;

import com.bookstudio.shared.enums.FineStatus;

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
