package com.bookstudio.loan.application.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateLoanRequest {
    private Long readerId;
    private String observation;
    private List<CreateLoanItemRequest> items;
}
