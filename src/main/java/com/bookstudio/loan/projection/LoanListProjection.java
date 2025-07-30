package com.bookstudio.loan.projection;

import java.time.LocalDate;

public interface LoanListProjection {
    Long getLoanId();
    String getCode();

    String getReaderId();
    String getReaderCode();
    String getReaderFullName();

    LocalDate getLoanDate();
}
