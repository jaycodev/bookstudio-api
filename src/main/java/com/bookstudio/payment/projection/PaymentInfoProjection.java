package com.bookstudio.payment.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PaymentInfoProjection {
    Long getPaymentId();
    String getCode();
    String getReaderFullName();
    BigDecimal getAmount();
    LocalDate getPaymentDate();
    String getMethod();
}
