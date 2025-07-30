package com.bookstudio.payment.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PaymentListProjection {
    Long getPaymentId();
    String getCode();
    String getReaderFullName();
    BigDecimal getAmount();
    LocalDate getPaymentDate();
    String getMethod();
}
