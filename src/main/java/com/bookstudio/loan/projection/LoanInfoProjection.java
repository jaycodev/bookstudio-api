package com.bookstudio.loan.projection;

import java.time.LocalDate;

public interface LoanInfoProjection {
    Long getLoanId();
    String getCode();

    String getReaderId();
    String getReaderCode();
    String getReaderFullName();

    LocalDate getLoanDate();
    String getObservation();
}
