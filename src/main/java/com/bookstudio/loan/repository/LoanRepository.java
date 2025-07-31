package com.bookstudio.loan.repository;

import com.bookstudio.loan.dto.LoanListDto;
import com.bookstudio.loan.model.Loan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
        SELECT new com.bookstudio.loan.dto.LoanListDto(
            l.code,
            r.code,
            CONCAT(r.firstName, ' ', r.lastName),
            l.loanDate,
            l.observation,
            l.loanId
        )
        FROM Loan l
        JOIN l.reader r
        ORDER BY l.loanId DESC
    """)
    List<LoanListDto> findList();
}
