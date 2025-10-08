package com.bookstudio.loan.application.dto.request;

import java.time.LocalDate;

public record CreateLoanItemRequest(
    Long copyId,
    LocalDate dueDate
) {}
