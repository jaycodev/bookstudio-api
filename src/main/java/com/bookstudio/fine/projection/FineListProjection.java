package com.bookstudio.fine.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FineListProjection {
    Long getFineId();
    String getCode();
    String getLoanCode();
    BigDecimal getAmount();
    Integer getDaysLate();
    String getStatus();
    LocalDate getIssuedAt();
}
