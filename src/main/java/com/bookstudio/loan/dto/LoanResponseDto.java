package com.bookstudio.loan.dto;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanResponseDto {
    private Long loanId;

    private String bookId;
    private String bookTitle;

    private String studentId;
    private String studentFullName;

    private LocalDate loanDate;
    private LocalDate returnDate;
    private Integer quantity;
    private String status;

    public String getFormattedLoanId() {
        return IdFormatter.formatId(String.valueOf(loanId), "P");
    }

    public String getFormattedBookId() {
        return IdFormatter.formatId(bookId, "L");
    }

    public String getFormattedStudentId() {
        return IdFormatter.formatId(studentId, "ES");
    }
}
