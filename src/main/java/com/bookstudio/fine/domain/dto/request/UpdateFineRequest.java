package com.bookstudio.fine.domain.dto.request;

import java.math.BigDecimal;

import com.bookstudio.fine.domain.model.type.FineStatus;

import lombok.Data;

@Data
public class UpdateFineRequest {
    private BigDecimal amount;
    private Integer daysLate;
    private FineStatus status;
}
