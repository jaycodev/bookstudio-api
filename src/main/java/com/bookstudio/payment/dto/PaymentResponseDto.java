package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponseDto {
    private Long paymentId;
    private String code;
    private String readerFullName;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
}
