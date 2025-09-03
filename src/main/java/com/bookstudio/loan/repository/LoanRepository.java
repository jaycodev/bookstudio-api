package com.bookstudio.loan.repository;

import com.bookstudio.loan.dto.LoanListRawDto;
import com.bookstudio.loan.model.Loan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
        SELECT new com.bookstudio.loan.dto.LoanListRawDto(
            l.loanId,
            l.code,
            r.code,
            CONCAT(r.firstName, ' ', r.lastName),
            l.loanDate,
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.PRESTADO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.DEVUELTO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.RETRASADO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.EXTRAVIADO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.CANCELADO THEN 1 ELSE 0 END)
        )
        FROM Loan l
        JOIN l.reader r
        LEFT JOIN l.loanItems li
        GROUP BY l.loanId, l.code, r.code, r.firstName, r.lastName, l.loanDate
        ORDER BY l.loanId DESC
    """)
    List<LoanListRawDto> findRawList();

    @Query("""
        SELECT new com.bookstudio.loan.dto.LoanListRawDto(
            l.loanId,
            l.code,
            r.code,
            CONCAT(r.firstName, ' ', r.lastName),
            l.loanDate,
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.PRESTADO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.DEVUELTO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.RETRASADO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.EXTRAVIADO THEN 1 ELSE 0 END),
            SUM(CASE WHEN li.status = com.bookstudio.loan.relation.LoanItemStatus.CANCELADO THEN 1 ELSE 0 END)
        )
        FROM Loan l
        JOIN l.reader r
        LEFT JOIN l.loanItems li
        WHERE l.loanId = :loanId
        GROUP BY l.loanId, l.code, r.code, r.firstName, r.lastName, l.loanDate
    """)
    LoanListRawDto findRawById(Long loanId);
}
