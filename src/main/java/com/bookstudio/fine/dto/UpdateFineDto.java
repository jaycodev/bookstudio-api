package com.bookstudio.fine.dto;

import java.math.BigDecimal;

import com.bookstudio.fine.model.FineStatus;

import lombok.Data;

@Data
public class UpdateFineDto {
    private BigDecimal amount;
    private Integer daysLate;
    private FineStatus status;
}
