package com.bookstudio.loan.projection;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

public interface LoanInfoProjection {
    Long getLoanId();

    String getBookId();
    String getBookTitle();

    String getStudentId();
    String getStudentFullName();

    LocalDate getLoanDate();
    LocalDate getReturnDate();

    Integer getQuantity();
    String getObservation();
    String getStatus();

    default String getFormattedLoanId() {
        return IdFormatter.formatId(String.valueOf(getLoanId()), "P");
    }

    default String getFormattedBookId() {
        return IdFormatter.formatId(String.valueOf(getBookId()), "L");
    }

    default String getFormattedStudentId() {
        return IdFormatter.formatId(String.valueOf(getStudentId()), "ES");
    }
}
