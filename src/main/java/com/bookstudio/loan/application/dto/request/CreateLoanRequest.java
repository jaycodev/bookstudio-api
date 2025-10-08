package com.bookstudio.loan.application.dto.request;

import java.util.List;

public record CreateLoanRequest(
    Long readerId,
    String observation,
    List<CreateLoanItemRequest> items
) {}
