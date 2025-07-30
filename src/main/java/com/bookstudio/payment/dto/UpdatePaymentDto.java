package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bookstudio.shared.enums.PaymentMethod;
import lombok.Data;

@Data
public class UpdatePaymentDto {
    private Long paymentId;
    private Long readerId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod method;
}
