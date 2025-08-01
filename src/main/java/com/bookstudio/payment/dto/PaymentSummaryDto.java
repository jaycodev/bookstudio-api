package com.bookstudio.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSummaryDto {
    private Long id;
    private String code;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String method;
}
