package com.bookstudio.loan.repository;

import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.projection.LoanInfoProjection;
import com.bookstudio.loan.projection.LoanListProjection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
        SELECT 
            l.loanId AS loanId,
            l.code AS code,

            r.readerId AS readerId,
            r.code AS readerCode,
            CONCAT(r.firstName, ' ', r.lastName) AS readerFullName,

            l.loanDate AS loanDate
        FROM Loan l
        JOIN l.reader r
        ORDER BY l.loanId DESC
    """)
    List<LoanListProjection> findList();

    @Query("""
        SELECT 
            l.loanId AS loanId,
            l.code AS code,

            r.readerId AS readerId,
            r.code AS readerCode,
            CONCAT(r.firstName, ' ', r.lastName) AS readerFullName,

            l.loanDate AS loanDate,
            l.observation AS observation
        FROM Loan l
        JOIN l.reader r
        WHERE l.loanId = :id
    """)
    Optional<LoanInfoProjection> findInfoById(@Param("id") Long id);
}
