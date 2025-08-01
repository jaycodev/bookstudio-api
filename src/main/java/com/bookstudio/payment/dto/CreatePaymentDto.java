package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.bookstudio.shared.enums.PaymentMethod;
import lombok.Data;

@Data
public class CreatePaymentDto {
    private Long readerId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod method;
    private List<Long> fineIds;
}
