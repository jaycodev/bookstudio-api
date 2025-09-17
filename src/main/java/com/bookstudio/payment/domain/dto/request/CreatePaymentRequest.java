package com.bookstudio.payment.domain.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bookstudio.payment.domain.model.type.PaymentMethod;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long readerId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod method;
    private List<Long> fineIds;
}
