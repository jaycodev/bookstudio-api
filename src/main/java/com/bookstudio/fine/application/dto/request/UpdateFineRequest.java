package com.bookstudio.fine.application.dto.request;

import java.math.BigDecimal;

import com.bookstudio.fine.domain.model.type.FineStatus;

public record UpdateFineRequest(
    BigDecimal amount,
    Integer daysLate,
    FineStatus status
) {}
