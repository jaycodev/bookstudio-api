package com.bookstudio.fine.dto;

import java.math.BigDecimal;

import com.bookstudio.shared.enums.FineStatus;
import lombok.Data;

@Data
public class UpdateFineDto {
    private BigDecimal amount;
    private Integer daysLate;
    private FineStatus status;
}
