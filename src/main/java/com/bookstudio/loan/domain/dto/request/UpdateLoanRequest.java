package com.bookstudio.loan.domain.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateLoanRequest {
    private Long readerId;
    private String observation;
    private List<UpdateLoanItemRequest> items;
}
