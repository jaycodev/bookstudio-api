package com.bookstudio.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstudio.loan.dto.LoanItemSummaryDto;
import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.relation.LoanItem;
import com.bookstudio.loan.relation.LoanItemId;

public interface LoanItemRepository extends JpaRepository<LoanItem, LoanItemId> {
    List<LoanItem> findAllByOrderByIdDesc();
    
    void deleteAllByLoan(Loan loan);

    @Query("""
        SELECT new com.bookstudio.loan.dto.LoanItemSummaryDto(
            new com.bookstudio.copy.dto.CopySummaryDto(
                c.copyId, c.code, c.barcode, c.isAvailable
            ),
            li.dueDate,
            li.returnDate,
            li.status
        )
        FROM LoanItem li
        JOIN li.copy c
        WHERE li.loan.loanId = :loanId
    """)
    List<LoanItemSummaryDto> findLoanItemSummariesByLoanId(@Param("loanId") Long loanId);
}
