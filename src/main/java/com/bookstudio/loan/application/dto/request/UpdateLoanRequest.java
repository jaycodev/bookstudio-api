package com.bookstudio.loan.application.dto.request;

import java.util.List;

public record UpdateLoanRequest(
    Long readerId,
    String observation,
    List<UpdateLoanItemRequest> items
) {}
